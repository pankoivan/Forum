package org.forum.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "forum_user_forum_role")
public class AssignedRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @ManyToOne
    @JoinColumn(name = "forum_user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "forum_role_id")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User userWhoAssigned;

}
