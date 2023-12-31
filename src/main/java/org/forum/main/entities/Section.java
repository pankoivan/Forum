package org.forum.main.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.forum.auxiliary.constants.DateTimeFormatConstants;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {
        "topics"
})
@ToString(exclude = {
        "topics"
})
@Entity
@Table(name = "forum_section")
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotEmpty(message = "Название раздела не должно быть пустым")
    @NotBlank(message = "Название раздела не должно содержать только пробелы")
    @Size(min = 4, max = 80, message = "Минимальная длина названия раздела - 4 символа, максимальная - 80 символов")
    @Column(name = "name")
    private String name;

    @NotEmpty(message = "Описание раздела не должно быть пустым")
    @NotBlank(message = "Описание раздела не должно содержать только пробелы")
    @Size(min = 24, max = 200, message = "Минимальная длина описания раздела - 24 символа, максимальная - 200 символов")
    @Column(name = "description")
    private String description;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User userWhoCreated;

    @OneToMany(mappedBy = "section")
    private List<Topic> topics = new ArrayList<>();

    public String getFormattedCreationDate() {
        return creationDate.format(DateTimeFormatConstants.DAY_MONTH_YEAR_IN_HOUR_MINUTE_SECOND);
    }

    public boolean hasTopics() {
        return !topics.isEmpty();
    }

    public Integer topicsCount() {
        return topics.size();
    }

    public Integer messagesCount() {
        return topics.stream()
                .mapToInt(Topic::messagesCount)
                .sum();
    }

    public Message recentMessage() {
        return topics.stream()
                .flatMap(topic -> topic.getMessages().stream())
                .max(Comparator.comparing(Message::getCreationDate))
                .orElse(null);
    }

}
