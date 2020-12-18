package crow.teomant.modular.handshakes.user.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class UserCriteria {
    private String firstname = "";
    private String patronymic = "";
    private String lastname = "";
}
