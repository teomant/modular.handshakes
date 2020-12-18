package crow.teomant.modular.handshakes.user.persistance.model;

import crow.teomant.modular.handshakes.user.domain.model.RelationType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;

@Embeddable
@Data
public class RelationEntity {
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RelationType relationType;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "to_id")
    private UserEntity user;
}
