package crow.teomant.modular.handshakes.user.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

@Data
@EqualsAndHashCode(of = "id")
public class User {
    private Long id;
    private String firstname;
    private String patronymic;
    private String lastname;
    private Integer telegramId;
    private String about = "";
    private List<Relation> relations = new ArrayList<>();

    public String getFio() {
        return Stream.of(lastname, firstname, patronymic)
            .filter(StringUtils::isNotBlank)
            .collect(Collectors.joining(" "));
    }

}
