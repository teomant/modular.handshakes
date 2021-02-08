package crow.teomant.modular.handshakes.user.web.controller;

import crow.teomant.modular.handshakes.common.relation.RelationType;
import crow.teomant.modular.handshakes.user.domain.model.User;
import crow.teomant.modular.handshakes.user.domain.model.UserCriteria;
import crow.teomant.modular.handshakes.user.domain.exchange.service.RestService;
import crow.teomant.modular.handshakes.user.domain.service.UserService;
import crow.teomant.modular.handshakes.user.web.dto.PathDto;
import crow.teomant.modular.handshakes.user.web.dto.UserDto;
import crow.teomant.modular.handshakes.user.web.mapper.UserDtoMapper;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final RestService restService;
    private final UserDtoMapper dtoMapper = new UserDtoMapper();

    @PostMapping("/user/add")
    public UserDto register(@RequestBody UserDto dto) {

        return dtoMapper.toDto(userService.save(dtoMapper.toDomain(dto)), true);
    }

    @GetMapping("/user/{id}")
    public UserDto get(@PathVariable Long id) {
        return dtoMapper.toDto(userService.findById(id), true);
    }

    @PostMapping("/user/{id}")
    public UserDto update(@PathVariable Long id, @RequestBody UserDto dto) {
        User toUpdate = dtoMapper.toDomain(dto);
        toUpdate.setId(id);
        return dtoMapper.toDto(userService.update(toUpdate), true);
    }

    @PostMapping("/user/{id}/relation")
    public UserDto createRelation(@PathVariable Long id, @RequestParam Long to,
                                  @RequestParam RelationType relationType) {
        return dtoMapper.toDto(userService.addRelation(id, to, relationType), true);
    }

    @PostMapping("/user/rebuild-graph")
    public void rebuildGraph() {
        restService.rebuildGraph();
    }

    @GetMapping("/user/{from}/get-path")
    public List<PathDto> getPath(@PathVariable Long from, @RequestParam Long to) {
        return restService.getPath(from, to).stream()
            .map(path -> {
                PathDto pathDto = new PathDto();
                pathDto.setRelation(path.getRelation());
                pathDto.setFrom(dtoMapper.toDto(path.getFrom(), false));
                pathDto.setTo(dtoMapper.toDto(path.getTo(), false));

                return pathDto;
            }).collect(Collectors.toList());
    }

    @GetMapping("/user/criteria")
    public List<UserDto> getByCriteria(@RequestParam String firstname, @RequestParam String patronymic,
                                       @RequestParam String lastname) {
        return userService.findByCriteria(UserCriteria.of(firstname, patronymic, lastname)).stream()
            .map(model -> dtoMapper.toDto(model, false)).collect(
                Collectors.toList());
    }

}
