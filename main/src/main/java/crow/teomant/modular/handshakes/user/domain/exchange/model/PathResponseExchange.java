package crow.teomant.modular.handshakes.user.domain.exchange.model;

import java.util.List;
import lombok.Data;

@Data
public class PathResponseExchange {

    Long responseTo;
    List<RelationExchange> path;
}
