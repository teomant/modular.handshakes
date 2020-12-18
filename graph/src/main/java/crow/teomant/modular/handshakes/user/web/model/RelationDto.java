package crow.teomant.modular.handshakes.user.web.model;

import crow.teomant.modular.handshakes.user.persistance.model.RelationType;
import lombok.Data;

@Data
public class RelationDto {
    private RelationType relationType;
    private Long personFrom;
    private Long personTo;
}