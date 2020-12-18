package crow.teomant.modular.handshakes.user.persistance.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.annotation.QueryResult;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@QueryResult
public class PathResult {
    private List<UserNode> path;
    private List<RelationRelationship> relations;
}