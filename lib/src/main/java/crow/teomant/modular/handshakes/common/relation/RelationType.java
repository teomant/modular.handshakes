package crow.teomant.modular.handshakes.common.relation;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.neo4j.ogm.typeconversion.AttributeConverter;

@AllArgsConstructor
@Getter
public enum RelationType {
    RELATIVE("родственник", 1),
    FRIEND("друг", 1),
    KNOW("знает", 1),
    DONT_KNOW("не знает", 2);

    private String value;
    private Integer priority;

}
