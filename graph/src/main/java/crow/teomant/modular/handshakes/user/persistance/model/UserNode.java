package crow.teomant.modular.handshakes.user.persistance.model;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(of = "id")
@NodeEntity(label = "USER")
public class UserNode {
    @Id
    @GeneratedValue
    private Long id;

    @Property
    private Long userId;

    @Relationship(type = "KNOWS")
    private List<RelationRelationship> relations = new ArrayList<>();
}
