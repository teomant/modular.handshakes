package crow.teomant.modular.handshakes.user.mq.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class PathResponseExchange {

    Long responseTo;
    List<RelationExchange> path = new ArrayList<>();
}
