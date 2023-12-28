package org.forum.main.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.forum.auxiliary.constants.DateTimeFormatConstants;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {
        "createdSections",
        "createdTopics",
        "postedMessages",
        "likes",
        "dislikes",
        "bans",
        "assignedBans"
})
@ToString(exclude = {
        "createdSections",
        "createdTopics",
        "postedMessages",
        "likes",
        "dislikes",
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

    @NotEmpty(message = "Ник не должен быть пустым")
    @NotBlank(message = "Ник не должен содержать только пробелы")
    @Size(min = 3, max = 24, message = "Минимальная длина ника - 3 символа, максимальная - 24 символа")
    @Column(name = "username")
    private String nickname;

    @NotEmpty(message = "Почта не должна быть пустой")
    @NotBlank(message = "Почта не должна содержать только пробелы")
    @Size(min = 12, max = 64, message = "Минимальная длина почты - 12 символов, максимальная - 64 символа")
    @Column(name = "email")
    private String email;

    @NotEmpty(message = "Пароль не должен быть пустым")
    @NotBlank(message = "Пароль не должен содержать только пробелы")
    @Size(min = 3, max = 128, message = "Минимальная длина пароля - 3 символа, максимальная - 128 символов")
    @Column(name = "password")
    private String password;

    @Column(name = "creation_date")
    private LocalDateTime registrationDate;

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
    private List<Like> likes;

    @OneToMany(mappedBy = "user")
    private List<Dislike> dislikes;

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

    public Ban getCurrentBan() {
        return bans.stream()
                .filter(ban -> ban.getEndDate().isAfter(LocalDate.now()))
                .findAny()
                .orElse(null);
    }

    public boolean isBanned() {
        return getCurrentBan() != null;
    }

    public boolean isActive() {
        return !isBanned();
    }

    public int getBansCount() {
        return bans.size();
    }

    public int getAssignedBansCount() {
        return assignedBans.size();
    }

    public String getFormattedRegistrationDate() {
        return registrationDate.format(DateTimeFormatConstants.DAY_MONTH_YEAR_IN_HOUR_MINUTE_SECOND);
    }

    public int getSectionsCount() {
        return createdSections.size();
    }

    public int getTopicsCount() {
        return createdTopics.size();
    }

    public int getMessagesCount() {
        return postedMessages.size();
    }

    public int getLikesCount() {
        return postedMessages.stream()
                .mapToInt(Message::likesCount)
                .sum();
    }

    public int getDislikesCount() {
        return postedMessages.stream()
                .mapToInt(Message::dislikesCount)
                .sum();
    }

    public int getReputation() {
        return getLikesCount() - getDislikesCount();
    }

    public int getAssignedLikesCount() {
        return likes.size();
    }

    public int getAssignedDislikesCount() {
        return dislikes.size();
    }

    public List<Message> getRecentMessages(int limit) {
        return postedMessages.stream()
                .sorted(Comparator.comparing(Message::getCreationDate))
                .limit(limit)
                .toList();
    }

    public List<Message> getRecentMessages() {
        return getRecentMessages(3);
    }

    public List<Message> getRecentLikedMessages(int limit) {
        return likes.stream()
                .map(Like::getMessage)
                .sorted(Comparator.comparing(Message::getCreationDate))
                .limit(limit)
                .toList();
    }

    public List<Message> getRecentLikedMessages() {
        return getRecentLikedMessages(3);
    }

    public List<Message> getRecentDislikedMessages(int limit) {
        return dislikes.stream()
                .map(Dislike::getMessage)
                .sorted(Comparator.comparing(Message::getCreationDate))
                .limit(limit)
                .toList();
    }

    public List<Message> getRecentDislikedMessages() {
        return getRecentDislikedMessages(3);
    }

    public boolean hasRole(String roleName) {
        return role.getName().equals(roleName);
    }

    public boolean hasAuthority(String authorityName) {
        return role.getAuthorities().stream()
                .anyMatch(authority -> authority.getName().equals(authorityName));
    }

    private Collection<? extends GrantedAuthority> getRoleAndAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>(role.getAuthorities());
        authorities.add(role);
        return authorities;
    }

}
