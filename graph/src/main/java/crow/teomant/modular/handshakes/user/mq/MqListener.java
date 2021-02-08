package crow.teomant.modular.handshakes.user.mq;

import crow.teomant.modular.handshakes.common.exchange.ExchangeDto;
import crow.teomant.modular.handshakes.common.exchange.ExchangeType;
import crow.teomant.modular.handshakes.common.exchange.PathExchange;
import crow.teomant.modular.handshakes.common.exchange.PathResponseExchange;
import crow.teomant.modular.handshakes.common.exchange.RebuildExchange;
import crow.teomant.modular.handshakes.common.exchange.RelationExchange;
import crow.teomant.modular.handshakes.common.exchange.UserExchange;
import crow.teomant.modular.handshakes.common.relation.RelationType;
import crow.teomant.modular.handshakes.user.persistance.model.RelationRelationship;
import crow.teomant.modular.handshakes.user.persistance.model.UserNode;
import crow.teomant.modular.handshakes.user.persistance.repository.RelationRelationshipRepository;
import crow.teomant.modular.handshakes.user.persistance.repository.UserNodeRepository;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MqListener {

    private final RabbitTemplate rabbitTemplate;
    private final UserNodeRepository userNodeRepository;
    private final RelationRelationshipRepository relationRelationshipRepository;

    @Value("#{${distances}}")
    private Map<RelationType, Long> distances;

    @RabbitListener(queues = "to_graph")
    public void process(ExchangeDto exchangeDto) {
        if (exchangeDto.getType().equals(ExchangeType.USER)) {
            addUser((UserExchange) exchangeDto);
        }
        if (exchangeDto.getType().equals(ExchangeType.RELATION)) {
            addRelation((RelationExchange) exchangeDto);
        }
        if (exchangeDto.getType().equals(ExchangeType.PATH)) {
            path((PathExchange) exchangeDto);
        }
        if (exchangeDto.getType().equals(ExchangeType.REBUILD)) {
            rebuild((RebuildExchange) exchangeDto);
        }
    }


    public void addUser(UserExchange userExchange) {

        UserNode userNode = new UserNode();
        userNode.setUserId(userExchange.getUserId());
        userNodeRepository.save(userNode);
    }

    public void addRelation(RelationExchange relationExchange) {

        UserNode personFrom = userNodeRepository.findByUserId(relationExchange.getPersonFrom())
            .orElseThrow(IllegalArgumentException::new);
        UserNode personTo =
            userNodeRepository.findByUserId(relationExchange.getPersonTo()).orElseThrow(IllegalArgumentException::new);


        RelationRelationship relationRelationship = personFrom.getRelations().stream()
            .filter(relation -> relation.getPersonTo().equals(personTo))
            .findFirst()
            .orElseGet(() -> {
                RelationRelationship relationship = new RelationRelationship();
                relationship.setPersonFrom(personFrom);
                relationship.setPersonTo(personTo);

                return relationship;
            });

        relationRelationship.setRelationType(relationExchange.getRelationType());
        relationRelationship.setDistance(distances.getOrDefault(relationExchange.getRelationType(), 20l));

        relationRelationshipRepository.save(relationRelationship);
    }

    public void path(PathExchange pathExchange) {

        PathResponseExchange pathResponseExchange = new PathResponseExchange();
        pathResponseExchange.setResponseTo(pathExchange.getResponseTo());

        userNodeRepository.findShortestPath(pathExchange.getFrom(), pathExchange.getTo())
            .ifPresent(result -> result.getRelations().forEach(relation -> {
                pathResponseExchange.getPath().add(
                    new RelationExchange(relation.getRelationType(), relation.getPersonFrom().getUserId(),
                        relation.getPersonTo().getUserId()));
            }));

        rabbitTemplate.convertAndSend("to_main", pathResponseExchange);
    }

    public void rebuild(RebuildExchange exchange) {
        userNodeRepository.deleteAll();
    }
}
