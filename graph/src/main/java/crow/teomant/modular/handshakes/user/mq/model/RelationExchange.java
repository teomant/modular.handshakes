package crow.teomant.modular.handshakes.user.mq.model;

import crow.teomant.modular.handshakes.user.persistance.model.RelationType;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelationExchange {
    private RelationType relationType;
    private Long personFrom;
    private Long personTo;
}