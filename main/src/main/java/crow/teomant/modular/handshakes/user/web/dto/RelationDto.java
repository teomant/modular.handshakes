package crow.teomant.modular.handshakes.user.web.dto;

import crow.teomant.modular.handshakes.user.domain.model.RelationType;
import lombok.Data;

@Data
public class RelationDto {
    UserDto to;
    RelationType relationType;
}
