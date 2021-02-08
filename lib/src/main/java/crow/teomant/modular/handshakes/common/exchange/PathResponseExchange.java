package crow.teomant.modular.handshakes.common.exchange;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class PathResponseExchange extends ExchangeDto {

    private Long responseTo;
    private List<RelationExchange> path = new ArrayList<>();

    @JsonCreator
    public PathResponseExchange(@JsonProperty("responseTo") Long responseTo,
                                @JsonProperty("path") List<RelationExchange> path) {
        super(ExchangeType.PATH_RESPONSE);
        this.responseTo = responseTo;
        this.path = path;
    }

    public PathResponseExchange() {
        super(ExchangeType.PATH_RESPONSE);
    }
}
