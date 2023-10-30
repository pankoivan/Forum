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
        "likes",
        "dislikes"
})
@ToString(exclude = {
        "likes",
        "dislikes"
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
    private List<Like> likes;

    @OneToMany(mappedBy = "message")
    private List<Dislike> dislikes;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

    public String getFormattedCreationDate() {
        return creationDate.format(DateTimeFormatConstants.DAY_MONTH_YEAR_IN_HOUR_MINUTE_SECOND);
    }

    public String getFormattedEditingDate() {
        return editingDate.format(DateTimeFormatConstants.DAY_MONTH_YEAR_IN_HOUR_MINUTE_SECOND);
    }

    public boolean hasLikes() {
        return !likes.isEmpty();
    }

    public int likesCount() {
        return likes.size();
    }

    public int dislikesCount() {
        return dislikes.size();
    }

    public List<User> getLikedUsers() {
        return likes.stream()
                .map(Like::getUser)
                .toList();
    }

    public List<User> getDislikedUsers() {
        return dislikes.stream()
                .map(Dislike::getUser)
                .toList();
    }

    public boolean containsLikedUserById(Integer likedUserId) {
        return likes.stream()
                .map(Like::getUser)
                .anyMatch(user -> user.getId().equals(likedUserId));
    }

    public boolean containsDislikedUserById(Integer dislikedUserId) {
        return dislikes.stream()
                .map(Dislike::getUser)
                .anyMatch(user -> user.getId().equals(dislikedUserId));
    }

}
