package crow.teomant.modular.handshakes.user.mq.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class UserExchange {
    private Long userId;
    private List<RelationExchange> relations = new ArrayList<>();
}
