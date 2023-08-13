package org.forum.entities;

import jakarta.persistence.*;
import lombok.*;
import org.forum.entities.enums.AuthorityEnum;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {
        "roles"
})
@ToString(exclude = {
        "roles"
})
@Entity
@Table(name = "forum_authority")
public class Authority implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private AuthorityEnum authority;

    @ManyToMany(
            mappedBy = "authorities",
            fetch = FetchType.EAGER
    )
    private List<Role> roles;

    @Override
    public String getAuthority() {
        return authority.name();
    }

}
