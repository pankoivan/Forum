package org.forum.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.forum.utils.BansUtils.isBanned;

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
public class User implements UserDetails {

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

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @OneToOne(mappedBy = "user")
    private UserInformation userInformation;

    @OneToMany(mappedBy = "userWhoCreated")
    private List<Section> createdSections;

    @OneToMany(mappedBy = "userWhoCreated")
    private List<Topic> createdTopics;

    @OneToMany(mappedBy = "userWhoPosted")
    private List<Message> postedMessages;

    @ManyToMany(mappedBy = "usersWhoLiked")
    private List<Message> likedMessages;

    @OneToMany(mappedBy = "user")
    private List<Ban> bans;

    @OneToMany(mappedBy = "userWhoAssigned")
    private List<Ban> assignedBans;

    @ManyToOne
    @JoinColumn(name = "forum_role_id")
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>(role.getAuthorities());
        authorities.add(role);
        return authorities;
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
        return isBanned(this);
    }

    @Override
    public boolean isAccountNonLocked() {
        return isBanned(this);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isBanned(this);
    }

    @Override
    public boolean isEnabled() {
        return isBanned(this);
    }

}
