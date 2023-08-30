package org.forum.entities;

import jakarta.persistence.*;
import lombok.*;
import org.forum.entities.interfaces.ChronoGetter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {
        "createdSections",
        "createdTopics",
        "postedMessages",
        "likedMessages",
        "bans",
        "assignedBans"
})
@ToString(exclude = {
        "createdSections",
        "createdTopics",
        "postedMessages",
        "likedMessages",
        "bans",
        "assignedBans"
})
@Entity
@Table(name = "forum_user")
public class User implements UserDetails, ChronoGetter<LocalDateTime> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "username")
    private String nickname;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @OneToOne(
            mappedBy = "user",
            cascade = CascadeType.ALL
    )
    private UserInformation userInformation = new UserInformation();

    @OneToMany(mappedBy = "userWhoCreated")
    private List<Section> createdSections;

    @OneToMany(mappedBy = "userWhoCreated")
    private List<Topic> createdTopics;

    @OneToMany(mappedBy = "userWhoPosted")
    private List<Message> postedMessages;

    @OneToMany(mappedBy = "user")
    private List<Like> likedMessages;

    @OneToMany(
            mappedBy = "user",
            fetch = FetchType.EAGER
    )
    private List<Ban> bans;

    @OneToMany(mappedBy = "userWhoAssigned")
    private List<Ban> assignedBans;

    @ManyToOne
    @JoinColumn(name = "forum_role_id")
    private Role role;

    @Override
    public LocalDateTime get() {
        return creationDate;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoleAndAuthorities();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isActive();
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isActive();
    }

    @Override
    public boolean isEnabled() {
        return isActive();
    }

    private Collection<? extends GrantedAuthority> getRoleAndAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>(role.getAuthorities());
        authorities.add(role);
        return authorities;
    }

    private boolean isBanned() {
        return bans.stream()
                .anyMatch(ban -> LocalDate.now().isBefore(ban.getEndDate()));
    }

    private boolean isActive() {
        return !isBanned();
    }

}
