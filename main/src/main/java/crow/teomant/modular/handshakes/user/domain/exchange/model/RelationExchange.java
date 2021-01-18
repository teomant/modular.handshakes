package crow.teomant.modular.handshakes.user.domain.exchange.model;

import crow.teomant.modular.handshakes.user.domain.model.RelationType;
import lombok.Data;

@Data
public class RelationExchange {
    private RelationType relationType;
    private Long personFrom;
    private Long personTo;
}