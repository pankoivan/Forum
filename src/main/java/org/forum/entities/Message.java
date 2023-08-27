package org.forum.entities;

import jakarta.persistence.*;
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

    @Column(name = "message_text")
    private String messageText;

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

    public int likesCount() {
        return likedUsers.size();
    }

}
