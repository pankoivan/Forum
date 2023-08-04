package org.forum.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {
        "createdSections",
        "createdTopics",
        "postedMessages",
        "likedMessages",
        "roles",
        "assignedRoles"
})
@ToString(exclude = {
        "createdSections",
        "createdTopics",
        "postedMessages",
        "likedMessages",
        "roles",
        "assignedRoles"
})
@Entity
@Table(name = "forum_user")
public class User implements org.springframework.security.core.userdetails.UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @OneToOne(mappedBy = "user")
    private UserDetails userDetails;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @OneToMany(mappedBy = "userWhoCreated")
    private List<Section> createdSections;

    @OneToMany(mappedBy = "userWhoCreated")
    private List<Topic> createdTopics;

    @OneToMany(mappedBy = "userWhoPosted")
    private List<Message> postedMessages;

    @ManyToMany(mappedBy = "usersWhoLiked")
    private List<Message> likedMessages;

    @OneToMany(mappedBy = "user")
    private Set<AssignedRole> roles;

    @OneToMany(mappedBy = "userWhoAssigned")
    private List<AssignedRole> assignedRoles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(AssignedRole::getRole)
                .toList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

}
