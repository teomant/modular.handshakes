package crow.teomant.modular.handshakes.user.domain.rest.service.impl;

import crow.teomant.modular.handshakes.user.domain.model.Path;
import crow.teomant.modular.handshakes.user.domain.model.Relation;
import crow.teomant.modular.handshakes.user.domain.model.User;
import crow.teomant.modular.handshakes.user.domain.repository.UserRepository;
import crow.teomant.modular.handshakes.user.domain.rest.model.RelationRest;
import crow.teomant.modular.handshakes.user.domain.rest.model.UserRest;
import crow.teomant.modular.handshakes.user.domain.rest.service.RestService;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class RestServiceImpl implements RestService {

    private final UserRepository userRepository;

    @Override
    public void rebuildGraph() {
        RestTemplate restTemplate = new RestTemplate();
        String dropUrl = "http://localhost:8081/drop-graph";
        restTemplate.delete(dropUrl);

        List<User> users = userRepository.findAll();
        users.forEach(this::addNode);
        users.forEach(user -> user.getRelations().forEach(relation -> this.addRelation(user, relation)));

    }

    @Override
    public void addNode(User user) {
        RestTemplate restTemplate = new RestTemplate();
        String addUrl = "http://localhost:8081/add-user";

        UserRest userRest = new UserRest();
        userRest.setUserId(user.getId());

        HttpEntity<UserRest> request = new HttpEntity<>(userRest);
        UserRest response = restTemplate.postForObject(addUrl, request, UserRest.class);
    }

    @Override
    public void addRelation(User from, Relation relation) {
        RestTemplate restTemplate = new RestTemplate();
        String addUrl = "http://localhost:8081/add-relation";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(addUrl)
            .queryParam("from", from.getId())
            .queryParam("to", relation.getUser().getId())
            .queryParam("relationType", relation.getRelationType());

        RelationRest response = restTemplate.postForObject(
            builder.build().toUriString(),
            null,
            RelationRest.class
        );
    }

    @Override
    public List<Path> getPath(Long from, Long to) {
        RestTemplate restTemplate = new RestTemplate();
        String pathUrl = "http://localhost:8081/path";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(pathUrl)
            .queryParam("from", from)
            .queryParam("to", to);

        return Arrays.asList(
            restTemplate.getForObject(
                builder.build().toUriString(),
                RelationRest[].class
            )
        )
            .stream()
            .map(relationRest -> {
                Path path = new Path();
                path.setFrom(userRepository.findByIdWithoutRelations(relationRest.getPersonFrom()));
                path.setTo(userRepository.findByIdWithoutRelations(relationRest.getPersonTo()));
                path.setRelation(relationRest.getRelationType());
                return path;
            }).collect(Collectors.toList());

    }
}
