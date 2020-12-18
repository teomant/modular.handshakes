package crow.teomant.modular.handshakes.user.persistance.repository;

import crow.teomant.modular.handshakes.user.domain.model.User;
import crow.teomant.modular.handshakes.user.domain.model.UserCriteria;
import crow.teomant.modular.handshakes.user.domain.repository.UserRepository;
import crow.teomant.modular.handshakes.user.persistance.mapper.UserMapper;
import crow.teomant.modular.handshakes.user.persistance.model.UserEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserMapper userMapper = new UserMapper();

    @Override
    public User save(User user) {
        UserEntity toSave = userMapper.toPersistance(user);
        UserEntity entity = Objects.isNull(user.getId())
            ? new UserEntity()
            : userJpaRepository.findById(user.getId()).orElseThrow(IllegalArgumentException::new);

        entity.setFirstname(toSave.getFirstname());
        entity.setPatronymic(toSave.getPatronymic());
        entity.setLastname(toSave.getLastname());
        entity.setTelegramId(toSave.getTelegramId());
        entity.setAbout(toSave.getAbout());
        entity.setRelations(toSave.getRelations());

        return userMapper.toDomain(userJpaRepository.save(entity), true);
    }

    @Override
    public User findById(Long id) {
        return userMapper.toDomain(userJpaRepository.findById(id).orElseThrow(IllegalArgumentException::new), true);
    }

    @Override
    public Optional<User> findByTelegramId(Integer telegramId) {
        return userJpaRepository.findByTelegramId(telegramId).map(user -> userMapper.toDomain(user, false));
    }

    @Override
    public User findByIdWithoutRelations(Long id) {
        return userMapper.toDomain(userJpaRepository.findById(id).orElseThrow(IllegalArgumentException::new), false);
    }

    @Override
    public List<User> findAll() {
        return userJpaRepository.findAll().stream()
            .map(entity -> userMapper.toDomain(entity, true))
            .collect(Collectors.toList());
    }

    @Override
    public List<User> findByCriteria(UserCriteria criteria) {
        return userJpaRepository.findAll(getByCriteria(criteria)).stream()
            .map(entity -> userMapper.toDomain(entity, false)).collect(
                Collectors.toList());
    }

    private Specification<UserEntity> getByCriteria(UserCriteria criteria) {
        return (
            (userEntity, criteriaQuery, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();

                if (!criteria.getFirstname().isEmpty()) {
                    predicates
                        .add(criteriaBuilder.like(
                            criteriaBuilder.lower(userEntity.get("firstname")),
                            "%" + criteria.getFirstname().toLowerCase() + "%")
                        );
                    predicates
                        .add(criteriaBuilder.like(
                            criteriaBuilder.lower(userEntity.get("patronymic")),
                            "%" + criteria.getFirstname().toLowerCase() + "%")
                        );
                    predicates
                        .add(criteriaBuilder.like(
                            criteriaBuilder.lower(userEntity.get("lastname")),
                            "%" + criteria.getFirstname().toLowerCase() + "%")
                        );
                }

                if (!criteria.getPatronymic().isEmpty()) {
                    predicates
                        .add(criteriaBuilder.like(
                            criteriaBuilder.lower(userEntity.get("firstname")),
                            "%" + criteria.getPatronymic().toLowerCase() + "%")
                        );
                    predicates
                        .add(criteriaBuilder.like(
                            criteriaBuilder.lower(userEntity.get("patronymic")),
                            "%" + criteria.getPatronymic().toLowerCase() + "%")
                        );
                    predicates
                        .add(criteriaBuilder.like(
                            criteriaBuilder.lower(userEntity.get("lastname")),
                            "%" + criteria.getPatronymic().toLowerCase() + "%")
                        );
                }

                if (!criteria.getLastname().isEmpty()) {
                    predicates
                        .add(criteriaBuilder.like(
                            criteriaBuilder.lower(userEntity.get("firstname")),
                            "%" + criteria.getLastname().toLowerCase() + "%")
                        );
                    predicates
                        .add(criteriaBuilder.like(
                            criteriaBuilder.lower(userEntity.get("patronymic")),
                            "%" + criteria.getLastname().toLowerCase() + "%")
                        );
                    predicates
                        .add(criteriaBuilder.like(
                            criteriaBuilder.lower(userEntity.get("lastname")),
                            "%" + criteria.getLastname().toLowerCase() + "%")
                        );
                }

                return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
            }
        );
    }
}
