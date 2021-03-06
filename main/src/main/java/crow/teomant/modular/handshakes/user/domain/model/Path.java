package crow.teomant.modular.handshakes.user.domain.model;

import crow.teomant.modular.handshakes.common.relation.RelationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Path {
    private User from;
    private User to;
    private RelationType relation;
}
