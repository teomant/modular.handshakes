package crow.teomant.modular.handshakes.user.domain.service;

import crow.teomant.modular.handshakes.common.relation.RelationType;
import crow.teomant.modular.handshakes.user.domain.model.User;
import crow.teomant.modular.handshakes.user.domain.model.UserCriteria;
import java.util.List;
import java.util.Optional;

public interface UserService {

    User save(User toSave);

    User update(User toUpdate);

    User findById(Long id);

    User addRelation(Long from, Long to, RelationType relationType);

    List<User> findByCriteria(UserCriteria criteria);

    Optional<User> findByTelegramId(Integer id);
}
