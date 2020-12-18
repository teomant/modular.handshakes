package crow.teomant.modular.handshakes.user.web.mapper;

import crow.teomant.modular.handshakes.user.persistance.model.UserNode;
import crow.teomant.modular.handshakes.user.web.model.RelationDto;
import crow.teomant.modular.handshakes.user.web.model.UserDto;

import java.util.stream.Collectors;

public class UserMapper {

    public UserDto mapToDto(UserNode userNode) {
        UserDto user = new UserDto();
        user.setUserId(userNode.getUserId());
        user.setRelations(
                userNode.getRelations().stream()
                        .map(relationNode -> {
                            RelationDto relation = new RelationDto();
                            relation.setRelationType(relationNode.getRelationType());
                            relation.setPersonFrom(user.getUserId());
                            relation.setPersonTo(relationNode.getPersonTo().getUserId());
                            return relation;
                        }).collect(Collectors.toList())
        );

        return user;
    }
}
