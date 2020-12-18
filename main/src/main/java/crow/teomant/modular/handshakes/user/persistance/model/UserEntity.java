package crow.teomant.modular.handshakes.user.persistance.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@EqualsAndHashCode(of = "id")
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, insertable = false)
    private Long id;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String patronymic;

    @Column(nullable = false)
    private String lastname;

    @Column(nullable = false)
    private String about = "";

    @Column(name = "telegram_id")
    private Integer telegramId;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
        name = "relations",
        joinColumns = @JoinColumn(name = "from_id")
    )
    private List<RelationEntity> relations = new ArrayList<>();
}
