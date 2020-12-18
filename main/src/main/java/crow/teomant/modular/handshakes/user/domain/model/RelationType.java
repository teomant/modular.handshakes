package crow.teomant.modular.handshakes.user.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

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
