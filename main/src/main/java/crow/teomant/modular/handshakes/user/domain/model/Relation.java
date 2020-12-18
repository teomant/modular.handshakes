package crow.teomant.modular.handshakes.user.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Relation {
    private User user;
    private RelationType relationType;
}
