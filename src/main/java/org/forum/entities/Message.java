package org.forum.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {
        "usersWhoLiked"
})
@ToString(exclude = {
        "usersWhoLiked"
})
@Entity
@Table(name = "forum_message")
public class Message {

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

    @ManyToMany
    @JoinTable(
            name = "forum_like",
            joinColumns = @JoinColumn(name = "forum_message_id"),
            inverseJoinColumns = @JoinColumn(name = "forum_user_id")
    )
    private List<User> usersWhoLiked;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

}
