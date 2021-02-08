package crow.teomant.modular.handshakes.common.exchange;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = PathExchange.class, name = "PATH"),
    @JsonSubTypes.Type(value = PathResponseExchange.class, name = "PATH_RESPONSE"),
    @JsonSubTypes.Type(value = UserExchange.class, name = "USER"),
    @JsonSubTypes.Type(value = RelationExchange.class, name = "RELATION"),
    @JsonSubTypes.Type(value = RebuildExchange.class, name = "REBUILD") }
)
@AllArgsConstructor
@Getter
public abstract class ExchangeDto {
    private final ExchangeType type;
}
