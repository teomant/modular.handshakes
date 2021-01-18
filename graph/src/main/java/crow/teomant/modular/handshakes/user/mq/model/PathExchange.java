package crow.teomant.modular.handshakes.user.mq.model;

import java.io.Serializable;
import lombok.Data;

@Data
public class PathExchange {
    private Long from;
    private Long to;
    private Long responseTo;
}
