package crow.teomant.modular.handshakes.user.persistance.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.*;
import org.neo4j.ogm.annotation.typeconversion.Convert;

@Data
@EqualsAndHashCode(of = "id")
@RelationshipEntity(type = "KNOWS")
public class RelationRelationship {
    @Id
    @GeneratedValue
    private Long id;

    @Property
    private long distance;

    @Property
    @Convert(RelationType.Converter.class)
    private RelationType relationType;

    @StartNode
    private UserNode personFrom;

    @EndNode
    private UserNode personTo;
}