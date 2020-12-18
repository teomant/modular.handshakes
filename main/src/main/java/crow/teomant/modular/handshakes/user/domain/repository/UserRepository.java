package crow.teomant.modular.handshakes.user.domain.repository;

import crow.teomant.modular.handshakes.user.domain.model.User;
import crow.teomant.modular.handshakes.user.domain.model.UserCriteria;
import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    User findById(Long id);

    Optional<User> findByTelegramId(Integer telegramId);

    User findByIdWithoutRelations(Long id);

    List<User> findAll();

    List<User> findByCriteria(UserCriteria criteria);
}
