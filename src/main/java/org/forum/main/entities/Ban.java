package org.forum.main.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
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

    @NotEmpty(message = "Описание причины бана не должно быть пустым")
    @NotBlank(message = "Описание причины бана не должно содержать только пробелы")
    @Size(min = 15, max = 1024, message = "Минимальная длина описания причины бана - 15 символов, максимальная - 1024 символа")
    @Column(name = "reason")
    private String reason;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    public String getFormattedStartDate() {
        return startDate.format(DateTimeFormatConstants.DAY_MONTH_YEAR);
    }

    public String getFormattedEndDate() {
        return endDate.format(DateTimeFormatConstants.DAY_MONTH_YEAR);
    }

}
