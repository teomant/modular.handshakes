package crow.teomant.modular.handshakes.common.exchange;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import crow.teomant.modular.handshakes.common.relation.RelationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class RelationExchange extends ExchangeDto {

    private RelationType relationType;
    private Long personFrom;
    private Long personTo;

    public RelationExchange() {
        super(ExchangeType.RELATION);
    }
    @JsonCreator
    public RelationExchange(
        @JsonProperty("relationType") RelationType relationType,
        @JsonProperty("personFrom") Long personFrom,
        @JsonProperty("personTo") Long personTo) {
        super(ExchangeType.RELATION);
        this.relationType = relationType;
        this.personFrom = personFrom;
        this.personTo = personTo;
    }

}