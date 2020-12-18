package crow.teomant.modular.handshakes.user.persistance.model;

import lombok.RequiredArgsConstructor;
import org.neo4j.ogm.typeconversion.AttributeConverter;

import java.util.Arrays;

@RequiredArgsConstructor
public enum RelationType {
    RELATIVE,
    FRIEND,
    KNOW,
    DONT_KNOW;

    public static class Converter implements AttributeConverter<RelationType, String> {
        @Override
        public String toGraphProperty(RelationType relationType) {
            return relationType.name();
        }

        @Override
        public RelationType toEntityAttribute(String s) {
            return Arrays.stream(RelationType.values()).filter(d -> d.name().equals(s)).findFirst().orElseThrow(IllegalArgumentException::new);
        }
    }
}
