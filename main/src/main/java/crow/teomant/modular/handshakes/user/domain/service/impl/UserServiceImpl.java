package crow.teomant.modular.handshakes.user.domain.service.impl;

import crow.teomant.modular.handshakes.user.domain.model.Relation;
import crow.teomant.modular.handshakes.user.domain.model.RelationType;
import crow.teomant.modular.handshakes.user.domain.model.User;
import crow.teomant.modular.handshakes.user.domain.model.UserCriteria;
import crow.teomant.modular.handshakes.user.domain.repository.UserRepository;
import crow.teomant.modular.handshakes.user.domain.rest.service.RestService;
import crow.teomant.modular.handshakes.user.domain.service.UserService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RestService restService;

    @Override
    public User save(User toSave) {
        User save = userRepository.save(toSave);
        restService.addNode(save);
        return save;
    }

    @Override
    @Transactional
    public User update(User toUpdate) {
        User saved = userRepository.findById(toUpdate.getId());
        saved.setFirstname(toUpdate.getFirstname());
        saved.setPatronymic(toUpdate.getPatronymic());
        saved.setLastname(toUpdate.getLastname());
        saved.setAbout(toUpdate.getAbout());
        saved.setTelegramId(toUpdate.getTelegramId());

        return userRepository.save(saved);
    }

    @Override
    @Transactional
    public User findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional
    public User addRelation(Long fromId, Long toId, RelationType relationType) {

        if (fromId.equals(toId)) {
            throw new IllegalArgumentException();
        }

        User from = userRepository.findById(fromId);
        User to = userRepository.findById(toId);

        Relation relation = from.getRelations().stream()
            .filter(existingRelation -> existingRelation.getUser().getId().equals(toId))
            .findFirst()
            .orElseGet(() -> {
                Relation newRelation = new Relation();
                newRelation.setUser(to);
                from.getRelations().add(newRelation);

                return newRelation;
            });

        relation.setRelationType(relationType);

        User save = userRepository.save(from);

        restService.addRelation(save, relation);
        return save;
    }

    @Override
    public List<User> findByCriteria(UserCriteria criteria) {
        return userRepository.findByCriteria(criteria);
    }

    @Override
    public Optional<User> findByTelegramId(Integer id) {
        return userRepository.findByTelegramId(id);
    }
}
