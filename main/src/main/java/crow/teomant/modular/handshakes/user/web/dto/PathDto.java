package crow.teomant.modular.handshakes.user.web.dto;

import crow.teomant.modular.handshakes.common.relation.RelationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PathDto {
    private UserDto from;
    private UserDto to;
    private RelationType relation;
}
