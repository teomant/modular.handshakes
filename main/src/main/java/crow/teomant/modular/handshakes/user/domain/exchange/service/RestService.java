package crow.teomant.modular.handshakes.user.domain.exchange.service;

import crow.teomant.modular.handshakes.user.domain.model.Path;
import crow.teomant.modular.handshakes.user.domain.model.Relation;
import crow.teomant.modular.handshakes.user.domain.model.User;
import java.util.List;

public interface RestService {

    void rebuildGraph();

    void addNode(User user);

    void addRelation(User from, Relation relation);

    List<Path> getPath(Long from, Long to);

}
