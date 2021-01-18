package crow.teomant.modular.handshakes.user.domain.exchange.service.impl;

import crow.teomant.modular.handshakes.user.domain.exchange.model.PathExchange;
import crow.teomant.modular.handshakes.user.domain.exchange.model.PathResponseExchange;
import crow.teomant.modular.handshakes.user.domain.exchange.model.RelationExchange;
import crow.teomant.modular.handshakes.user.domain.exchange.model.UserExchange;
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
        rabbitTemplate.convertAndSend("rebuild", "rebuild");

        List<User> users = userRepository.findAll();
        users.forEach(this::addNode);
        users.forEach(user -> user.getRelations().forEach(relation -> this.addRelation(user, relation)));
    }

    @Override
    public void addNode(User user) {
        UserExchange userExchange = new UserExchange();
        userExchange.setUserId(user.getId());

        rabbitTemplate.convertAndSend("add_user", userExchange);
    }

    @Override
    public void addRelation(User from, Relation relation) {
        RelationExchange relationExchange = new RelationExchange();
        relationExchange.setPersonFrom(from.getId());
        relationExchange.setPersonTo(relation.getUser().getId());
        relationExchange.setRelationType(relation.getRelationType());

        rabbitTemplate.convertAndSend("add_relation", relationExchange);
    }

    @Override
    public void getPath(Long from, Long to, Long responseTo) {

        rabbitTemplate.convertAndSend("get_path", new PathExchange(from, to, responseTo));
    }

    @Override
    public void registerListener(MqListener listener) {
        listeners.add(listener);
    }

    @RabbitListener(queues = "path_response")
    public void pathListener(PathResponseExchange pathResponseExchange) {
        listeners.forEach(listener -> listener.processPath(pathResponseExchange));
    }
}
