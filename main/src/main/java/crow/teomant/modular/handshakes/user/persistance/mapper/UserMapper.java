package crow.teomant.modular.handshakes.user.persistance.mapper;

import crow.teomant.modular.handshakes.user.domain.model.Relation;
import crow.teomant.modular.handshakes.user.domain.model.User;
import crow.teomant.modular.handshakes.user.persistance.model.RelationEntity;
import crow.teomant.modular.handshakes.user.persistance.model.UserEntity;
import java.util.stream.Collectors;

public class UserMapper {

    public User toDomain(UserEntity userEntity, boolean mapRelations) {
        User user = new User();
        user.setId(userEntity.getId());
        user.setFirstname(userEntity.getFirstname());
        user.setPatronymic(userEntity.getPatronymic());
        user.setLastname(userEntity.getLastname());
        user.setTelegramId(userEntity.getTelegramId());
        user.setAbout(userEntity.getAbout());

        if (mapRelations) {
            user.setRelations(
                userEntity.getRelations().stream()
                    .map(relationEntity -> {
                        Relation relation = new Relation();
                        relation.setRelationType(relationEntity.getRelationType());
                        relation.setUser(this.toDomain(relationEntity.getUser(), false));

                        return relation;
                    }).collect(Collectors.toList())
            );
        }

        return user;
    }

    public UserEntity toPersistance(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(user.getId());
        userEntity.setFirstname(user.getFirstname());
        userEntity.setPatronymic(user.getPatronymic());
        userEntity.setLastname(user.getLastname());
        userEntity.setTelegramId(user.getTelegramId());
        userEntity.setAbout(user.getAbout());

        userEntity.setRelations(
            user.getRelations().stream()
                .map(relation -> {
                    RelationEntity relationEntity = new RelationEntity();
                    relationEntity.setRelationType(relation.getRelationType());
                    UserEntity to = new UserEntity();
                    to.setId(relation.getUser().getId());
                    relationEntity.setUser(to);

                    return relationEntity;
                }).collect(Collectors.toList())
        );

        return userEntity;
    }
}
