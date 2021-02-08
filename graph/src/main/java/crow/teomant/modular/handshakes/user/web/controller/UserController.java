package crow.teomant.modular.handshakes.user.web.controller;

import crow.teomant.modular.handshakes.common.relation.RelationType;
import crow.teomant.modular.handshakes.user.persistance.model.RelationRelationship;
import crow.teomant.modular.handshakes.user.persistance.model.UserNode;
import crow.teomant.modular.handshakes.user.persistance.repository.RelationRelationshipRepository;
import crow.teomant.modular.handshakes.user.persistance.repository.UserNodeRepository;
import crow.teomant.modular.handshakes.user.web.mapper.UserMapper;
import crow.teomant.modular.handshakes.user.web.model.RelationDto;
import crow.teomant.modular.handshakes.user.web.model.UserDto;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserNodeRepository userNodeRepository;
    private final RelationRelationshipRepository relationRelationshipRepository;
    private final UserMapper userMapper = new UserMapper();

    @Value("#{${distances}}")
    private Map<RelationType, Long> distances;

    @PostMapping("/add-user")
    public UserDto addUser(@RequestBody UserDto userDto) {

        UserNode userNode = new UserNode();
        userNode.setUserId(userDto.getUserId());
        return userMapper.mapToDto(userNodeRepository.save(userNode));
    }

    @PostMapping("/add-relation")
    public RelationDto addRelation(@RequestParam Long from, @RequestParam Long to, @RequestParam
        RelationType relationType) {

        UserNode personFrom = userNodeRepository.findByUserId(from).orElseThrow(IllegalArgumentException::new);
        UserNode personTo = userNodeRepository.findByUserId(to).orElseThrow(IllegalArgumentException::new);


        RelationRelationship relationRelationship = personFrom.getRelations().stream()
            .filter(relation -> relation.getPersonTo().equals(personTo))
            .findFirst()
            .orElseGet(() -> {
                RelationRelationship relationship = new RelationRelationship();
                relationship.setPersonFrom(personFrom);
                relationship.setPersonTo(personTo);

                return relationship;
            });

        relationRelationship.setRelationType(relationType);
        relationRelationship.setDistance(distances.getOrDefault(relationType, 20l));

        RelationRelationship save = relationRelationshipRepository.save(relationRelationship);

        RelationDto response = new RelationDto();
        response.setRelationType(save.getRelationType());
        response.setPersonFrom(save.getPersonFrom().getId());
        response.setPersonTo(save.getPersonTo().getId());

        return response;
    }

    @GetMapping("/path")
    public List<RelationDto> path(@RequestParam Long from, @RequestParam Long to) {

        return userNodeRepository.findShortestPath(from, to).map(shortestPath ->
            shortestPath.getRelations().stream()
                .map(relation -> {
                    RelationDto relationDto = new RelationDto();
                    relationDto.setRelationType(relation.getRelationType());
                    relationDto.setPersonFrom(relation.getPersonFrom().getUserId());
                    relationDto.setPersonTo(relation.getPersonTo().getUserId());

                    return relationDto;
                }).collect(Collectors.toList()))
            .orElse(Collections.emptyList());
    }

    @GetMapping("/user/{id}")
    public UserDto get(@PathVariable Long id) {

        return userNodeRepository.findById(id).map(userMapper::mapToDto).orElseThrow(IllegalArgumentException::new);

    }

    @DeleteMapping("/drop-graph")
    public ResponseEntity drop() {
        userNodeRepository.deleteAll();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh-relationships")
    public void refreshRelationships() {
        Iterable<RelationRelationship> all = relationRelationshipRepository.findAll();
        all.forEach(realtion ->
            realtion.setDistance(distances.getOrDefault(realtion.getRelationType(), 20l))
        );
        relationRelationshipRepository.saveAll(all);
    }

}
