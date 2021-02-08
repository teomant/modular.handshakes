package crow.teomant.modular.handshakes.user.domain.exchange.service.impl;

import crow.teomant.modular.handshakes.common.exchange.PathExchange;
import crow.teomant.modular.handshakes.common.exchange.PathResponseExchange;
import crow.teomant.modular.handshakes.common.exchange.RebuildExchange;
import crow.teomant.modular.handshakes.common.exchange.RelationExchange;
import crow.teomant.modular.handshakes.common.exchange.UserExchange;
import crow.teomant.modular.handshakes.user.domain.exchange.service.MqListener;
import crow.teomant.modular.handshakes.user.domain.exchange.service.MqService;
import crow.teomant.modular.handshakes.user.domain.model.Relation;
import crow.teomant.modular.handshakes.user.domain.model.User;
import crow.teomant.modular.handshakes.user.domain.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MqServiceImpl implements MqService {

    private final RabbitTemplate rabbitTemplate;
    private final UserRepository userRepository;

    private final List<MqListener> listeners = new ArrayList<>();

    @Override
    public void rebuildGraph() {
        rabbitTemplate.convertAndSend("to_graph", new RebuildExchange());

        List<User> users = userRepository.findAll();
        users.forEach(this::addNode);
        users.forEach(user -> user.getRelations().forEach(relation -> this.addRelation(user, relation)));
    }

    @Override
    public void addNode(User user) {
        UserExchange userExchange = new UserExchange();
        userExchange.setUserId(user.getId());

        rabbitTemplate.convertAndSend("to_graph", userExchange);
    }

    @Override
    public void addRelation(User from, Relation relation) {
        RelationExchange relationExchange = new RelationExchange();
        relationExchange.setPersonFrom(from.getId());
        relationExchange.setPersonTo(relation.getUser().getId());
        relationExchange.setRelationType(relation.getRelationType());

        rabbitTemplate.convertAndSend("to_graph", relationExchange);
    }

    @Override
    public void getPath(Long from, Long to, Long responseTo) {

        rabbitTemplate.convertAndSend("to_graph", new PathExchange(from, to, responseTo));
    }

    @Override
    public void registerListener(MqListener listener) {
        listeners.add(listener);
    }

    @RabbitListener(queues = "to_main")
    public void pathListener(PathResponseExchange pathResponseExchange) {
        listeners.forEach(listener -> listener.processPath(pathResponseExchange));
    }
}
