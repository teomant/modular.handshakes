package crow.teomant.modular.handshakes.user.domain.rest.model;

import crow.teomant.modular.handshakes.user.domain.model.RelationType;
import lombok.Data;

@Data
public class RelationRest {
    private RelationType relationType;
    private Long personFrom;
    private Long personTo;
}