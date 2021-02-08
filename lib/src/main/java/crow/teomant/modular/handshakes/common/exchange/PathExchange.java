package crow.teomant.modular.handshakes.common.exchange;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PathExchange extends ExchangeDto {

    private Long from;
    private Long to;
    private Long responseTo;

    @JsonCreator
    public PathExchange(
        @JsonProperty("from") Long from,
        @JsonProperty("to") Long to,
        @JsonProperty("responseTo") Long responseTo
    ) {
        super(ExchangeType.PATH);
        this.from = from;
        this.to = to;
        this.responseTo = responseTo;
    }
}
