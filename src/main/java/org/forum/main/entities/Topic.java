package org.forum.main.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.forum.auxiliary.constants.DateTimeFormatConstants;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {
        "messages"
})
@ToString(exclude = {
        "messages"
})
@Entity
@Table(name = "forum_topic")
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotEmpty(message = "Название темы не должно быть пустым")
    @NotBlank(message = "Название темы не должно содержать только пробелы")
    @Size(min = 4, max = 80, message = "Минимальная длина названия темы - 4 символа, максимальная - 80 символов")
    @Column(name = "name")
    private String name;

    @NotEmpty(message = "Описание темы не должно быть пустым")
    @NotBlank(message = "Описание темы не должно содержать только пробелы")
    @Size(min = 24, max = 200, message = "Минимальная длина описания темы - 24 символа, максимальная - 200 символов")
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

    public String getFormattedCreationDate() {
        return creationDate.format(DateTimeFormatConstants.DAY_MONTH_YEAR_IN_HOUR_MINUTE_SECOND);
    }

    public boolean hasMessages() {
        return !messages.isEmpty();
    }

    public int messagesCount() {
        return messages.size();
    }

    public int likesCount() {
        return messages.stream()
                .mapToInt(Message::likesCount)
                .sum();
    }

    public Message recentMessage() {
        return messages.stream()
                .max(Comparator.comparing(Message::getCreationDate))
                .orElse(null);
    }

}
