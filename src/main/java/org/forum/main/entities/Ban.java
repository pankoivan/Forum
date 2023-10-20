package org.forum.main.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.forum.auxiliary.constants.DateTimeFormatConstants;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ban_table")
public class Ban {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "forum_user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User userWhoAssigned;

    @Column(name = "reason")
    private String reason;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    public String getFormattedEndDate() {
        return endDate.format(DateTimeFormatConstants.DAY_MONTH_YEAR);
    }

}
