package crow.teomant.modular.handshakes.user.domain.exchange.service;

import crow.teomant.modular.handshakes.user.domain.model.Relation;
import crow.teomant.modular.handshakes.user.domain.model.User;

public interface MqService {

    void rebuildGraph();

    void addNode(User user);

    void addRelation(User from, Relation relation);

    void getPath(Long from, Long to, Long responseTo);

    void registerListener(MqListener listener);
}
