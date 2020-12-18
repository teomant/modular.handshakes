package crow.teomant.modular.handshakes.user.web.mapper;

import crow.teomant.modular.handshakes.user.domain.model.User;
import crow.teomant.modular.handshakes.user.web.dto.RelationDto;
import crow.teomant.modular.handshakes.user.web.dto.UserDto;
import java.util.stream.Collectors;

public class UserDtoMapper {

    public UserDto toDto(User user, boolean mapRelations) {
        UserDto userDto = new UserDto();
        userDto.setAbout(user.getAbout());
        userDto.setFirstname(user.getFirstname());
        userDto.setLastname(user.getLastname());
        userDto.setPatronymic(user.getPatronymic());
        userDto.setTelegramId(user.getTelegramId());
        userDto.setId(user.getId());

        if (mapRelations) {
            userDto.setRelations(
                user.getRelations().stream()
                    .map(relation -> {
                        RelationDto relationDto = new RelationDto();
                        relationDto.setRelationType(relation.getRelationType());
                        relationDto.setTo(toDto(relation.getUser(), false));
                        return relationDto;
                    }).collect(Collectors.toList())
            );
        }

        return userDto;
    }

    public User toDomain(UserDto dto) {
        User user = new User();
        user.setAbout(dto.getAbout());
        user.setFirstname(dto.getFirstname());
        user.setLastname(dto.getLastname());
        user.setPatronymic(dto.getPatronymic());
        user.setTelegramId(dto.getTelegramId());
        user.setId(dto.getId());

        return user;
    }
}
