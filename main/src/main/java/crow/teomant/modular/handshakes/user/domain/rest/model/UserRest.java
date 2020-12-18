package crow.teomant.modular.handshakes.user.domain.rest.model;


import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class UserRest {
    private Long userId;
    private List<RelationRest> relations = new ArrayList<>();
}
