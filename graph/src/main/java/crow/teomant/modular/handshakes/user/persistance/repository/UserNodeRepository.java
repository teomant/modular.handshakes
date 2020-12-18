package crow.teomant.modular.handshakes.user.persistance.repository;

import crow.teomant.modular.handshakes.user.persistance.model.PathResult;
import crow.teomant.modular.handshakes.user.persistance.model.UserNode;
import java.util.Optional;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

public interface UserNodeRepository extends Neo4jRepository<UserNode, Long> {

    @Query("MATCH (from:USER { userId:$u1 }), (to:USER { userId:$u2 }) , p = (from)-[:KNOWS*..6]->(to) " +
        "RETURN nodes(p) as path, relationships(p) as relations " +
        "ORDER BY reduce(distance = 0, r in relationships(p) | distance+r.distance) ASC " +
        "LIMIT 1")
    Optional<PathResult> findShortestPath(@Param("u1") Long u1, @Param("u2") Long u2);

    Optional<UserNode> findByUserId(Long userId);
}
