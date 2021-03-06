package crow.teomant.modular.handshakes.user.domain.exchange.service.impl;

import crow.teomant.modular.handshakes.common.exchange.RelationExchange;
import crow.teomant.modular.handshakes.common.exchange.UserExchange;
import crow.teomant.modular.handshakes.user.domain.model.Path;
import crow.teomant.modular.handshakes.user.domain.model.Relation;
import crow.teomant.modular.handshakes.user.domain.model.User;
import crow.teomant.modular.handshakes.user.domain.repository.UserRepository;
import crow.teomant.modular.handshakes.user.domain.exchange.service.RestService;
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

        UserExchange userRest = new UserExchange();
        userRest.setUserId(user.getId());

        HttpEntity<UserExchange> request = new HttpEntity<>(userRest);
        UserExchange response = restTemplate.postForObject(addUrl, request, UserExchange.class);
    }

    @Override
    public void addRelation(User from, Relation relation) {
        RestTemplate restTemplate = new RestTemplate();
        String addUrl = "http://localhost:8081/add-relation";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(addUrl)
            .queryParam("from", from.getId())
            .queryParam("to", relation.getUser().getId())
            .queryParam("relationType", relation.getRelationType());

        RelationExchange response = restTemplate.postForObject(
            builder.build().toUriString(),
            null,
            RelationExchange.class
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
                RelationExchange[].class
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
