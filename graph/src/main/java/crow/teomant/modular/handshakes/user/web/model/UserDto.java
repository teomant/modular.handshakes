package crow.teomant.modular.handshakes.user.web.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserDto {
    private Long userId;
    private List<RelationDto> relations = new ArrayList<>();

}
