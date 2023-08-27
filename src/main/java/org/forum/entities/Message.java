package org.forum.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.forum.entities.interfaces.LocalDateTimeGetter;

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
public class Message implements LocalDateTimeGetter {

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

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User userWhoPosted;

    @OneToMany(mappedBy = "message")
    private List<Like> likedUsers;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @Override
    public LocalDateTime get() {
        return creationDate;
    }

    public boolean hasLikes() {
        return !likedUsers.isEmpty();
    }

    public int likesCount() {
        return likedUsers.size();
    }

}
