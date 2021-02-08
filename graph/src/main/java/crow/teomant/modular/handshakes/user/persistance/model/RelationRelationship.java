package crow.teomant.modular.handshakes.user.persistance.model;

import crow.teomant.modular.handshakes.common.relation.RelationType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;
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
    @Convert(RelationshipTypeConverter.class)
    private RelationType relationType;

    @StartNode
    private UserNode personFrom;

    @EndNode
    private UserNode personTo;
}