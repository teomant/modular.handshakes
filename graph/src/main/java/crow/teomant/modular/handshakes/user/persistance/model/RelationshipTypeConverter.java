package crow.teomant.modular.handshakes.user.persistance.model;

import crow.teomant.modular.handshakes.common.relation.RelationType;
import java.util.Arrays;
import org.neo4j.ogm.typeconversion.AttributeConverter;

public class RelationshipTypeConverter  implements AttributeConverter<RelationType, String> {

        @Override
        public String toGraphProperty(RelationType relationType) {
            return relationType.name();
        }

        @Override
        public RelationType toEntityAttribute(String s) {
            return Arrays.stream(RelationType.values()).filter(d -> d.name().equals(s)).findFirst()
                .orElseThrow(IllegalArgumentException::new);
        }
}
