package crow.teomant.modular.handshakes.user.web.dto;

import crow.teomant.modular.handshakes.common.relation.RelationType;
import lombok.Data;

@Data
public class RelationDto {
    UserDto to;
    RelationType relationType;
}
