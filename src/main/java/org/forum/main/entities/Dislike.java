package org.forum.main.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.forum.auxiliary.constants.DateTimeFormatConstants;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "forum_dislike")
public class Dislike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @ManyToOne
    @JoinColumn(name = "forum_user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "forum_message_id")
    private Message message;

    public String getFormattedCreationDate() {
        return creationDate.format(DateTimeFormatConstants.DAY_MONTH_YEAR_IN_HOUR_MINUTE_SECOND);
    }

}
