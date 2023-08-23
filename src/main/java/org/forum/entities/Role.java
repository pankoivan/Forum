package org.forum.entities;

import jakarta.persistence.*;
import lombok.*;
import org.forum.entities.enums.RoleEnum;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {
        "users"
})
@ToString(exclude = {
        "users"
})
@Entity
@Table(name = "forum_role")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "forum_role_forum_authority",
            joinColumns = @JoinColumn(name = "forum_role_id"),
            inverseJoinColumns = @JoinColumn(name = "forum_authority_id")
    )
    private List<Authority> authorities;

    @OneToMany(mappedBy = "role")
    private List<User> users;

    @Override
    public String getAuthority() {
        return name;
    }

}
