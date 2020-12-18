package crow.teomant.modular.handshakes.user.web.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String firstname;
    private String patronymic;
    private String lastname;
    private Integer telegramId = null;
    private String about;
    List<RelationDto> relations = new ArrayList<>();
}
