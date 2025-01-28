package hu.rmegyesi.rmbk.userstorage.data;

import io.github.threetenjaxb.core.InstantXmlAdapter;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import java.time.Instant;

@Entity
@Table(name = "user_entity")
public class UserEntity extends PanacheEntityBase {

    @Id
    @SequenceGenerator(
            name = "userEntitySequence",
            sequenceName = "user_entity_id_seq",
            allocationSize = 1,
            initialValue = 2
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "userEntitySequence"
    )
    public Long id;

    public boolean enabled;

    public String username;

    @Column(name = "first_name")
    public String firstName;

    @Column(name = "last_name")
    public String lastName;

    public String email;

    @Column(name = "created_at")
    @XmlSchemaType(name = "dateTime")
    @XmlJavaTypeAdapter(InstantXmlAdapter.class)
    public Instant createdAt;

    public String password;
}
