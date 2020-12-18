package crow.teomant.modular.handshakes.user.persistance.repository;

import crow.teomant.modular.handshakes.user.persistance.model.RelationRelationship;
import crow.teomant.modular.handshakes.user.persistance.model.UserNode;
import java.util.Optional;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface RelationRelationshipRepository extends Neo4jRepository<RelationRelationship, Long> {

}
