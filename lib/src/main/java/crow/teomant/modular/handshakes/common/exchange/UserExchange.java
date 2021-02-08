package crow.teomant.modular.handshakes.common.exchange;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserExchange extends ExchangeDto {
    private Long userId;
    private List<RelationExchange> relations = new ArrayList<>();

    public UserExchange() {
        super(ExchangeType.USER);
    }

    @JsonCreator
    public UserExchange(@JsonProperty("userId") Long userId,
                        @JsonProperty("relations") List<RelationExchange> relations) {
        super(ExchangeType.USER);
        this.userId = userId;
        this.relations = relations;
    }
}
