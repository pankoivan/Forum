package org.forum.main.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.forum.auxiliary.constants.DateTimeFormatConstants;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {
        "likedUsers"
})
@ToString(exclude = {
        "likedUsers"
})
@Entity
@Table(name = "forum_message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotEmpty(message = "Сообщение не должно быть пустым")
    @NotBlank(message = "Сообщение не должно содержать только пробелы")
    @Size(min = 15, max = 4096, message = "Минимальная длина сообщения - 15 символов, максимальная - 4096 символов")
    @Column(name = "message_text")
    private String text;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "editing_date")
    private LocalDateTime editingDate;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User userWhoPosted;

    @OneToMany(mappedBy = "message")
    private List<Like> likedUsers;

    @OneToMany(mappedBy = "message")
    private List<Dislike> dislikedUsers;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

    public String getFormattedCreationDate() {
        return creationDate.format(DateTimeFormatConstants.SEPARATED_DATE_TIME);
    }

    public String getFormattedEditingDate() {
        return editingDate.format(DateTimeFormatConstants.SEPARATED_DATE_TIME);
    }

    public boolean hasLikes() {
        return !likedUsers.isEmpty();
    }

    public int likesCount() {
        return likedUsers.size();
    }

    public int dislikesCount() {
        return dislikedUsers.size();
    }

    public boolean containsLikedUserById(Integer likedUserId) {
        return likedUsers.stream()
                .map(Like::getUser)
                .anyMatch(user -> user.getId().equals(likedUserId));
    }

    public boolean containsDislikedUserById(Integer dislikedUserid) {
        return dislikedUsers.stream()
                .map(Dislike::getUser)
                .anyMatch(user -> user.getId().equals(dislikedUserid));
    }

    public List<User> getLikedUsers() {
        return likedUsers.stream()
                .map(Like::getUser)
                .toList();
    }

    public List<User> getDislikedUsers() {
        return dislikedUsers.stream()
                .map(Dislike::getUser)
                .toList();
    }

}
