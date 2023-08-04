package org.forum.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {
        "messages"
})
@ToString(exclude = {
        "messages"
})
@Entity
@Table(name = "topic")
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User userWhoCreated;

    @ManyToOne
    @JoinColumn(name = "forum_section_id")
    private Section section;

    @OneToMany(mappedBy = "topic")
    private List<Message> messages;

}
