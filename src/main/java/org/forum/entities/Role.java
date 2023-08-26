package org.forum.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
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

    @NotEmpty(message = "Название роли не должно быть пустым")
    @NotBlank(message = "Название роли не должно содержать только пробелы")
    @Size(min = 7, max = 32, message = "Минимальная длина названия роли - 7 символов, максимальная - 32 символа")
    @Pattern(regexp = "ROLE_.*", message = "Название роли должно начинаться с приставки \"ROLE_\"")
    @Column(name = "name")
    private String name;

    @NotEmpty(message = "Список прав для роли не должен быть пустым")
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "forum_role_forum_authority",
            joinColumns = @JoinColumn(name = "forum_role_id"),
            inverseJoinColumns = @JoinColumn(name = "forum_authority_id")
    )
    private List<Authority> authorities = new ArrayList<>();

    @OneToMany(mappedBy = "role")
    private List<User> users;

    @Override
    public String getAuthority() {
        return name;
    }

    public boolean hasUsers() {
        return !users.isEmpty();
    }

    public int authoritiesCount() {
        return authorities.size();
    }

    public void removeAuthority(Authority authority) {
        authorities.remove(authority);
    }

    public boolean containsAuthorityById(Integer authorityId) {
        return authorities.stream()
                .anyMatch(authority -> authority.getId().equals(authorityId));
    }

}
