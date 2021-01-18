package crow.teomant.modular.handshakes.user.domain.exchange.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PathExchange {
    private Long from;
    private Long to;
    private Long responseTo;
}
