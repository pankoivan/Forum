package org.forum.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
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

    @NotEmpty(message = "Название права не должно быть пустым")
    @NotBlank(message = "Название права не должно содержать только пробелы")
    @Size(min = 2, max = 128, message = "Минимальная длина названия права - 2 символа, максимальная - 128 символов")
    @Pattern(regexp = "^(?!ROLE_).*$", message = "Название права НЕ должно начинаться с приставки \"ROLE_\"")
    @Column(name = "name")
    private String name;

    @ManyToMany(
            mappedBy = "authorities",
            fetch = FetchType.EAGER
    )
    private List<Role> roles;

    @Override
    public String getAuthority() {
        return name;
    }

}
