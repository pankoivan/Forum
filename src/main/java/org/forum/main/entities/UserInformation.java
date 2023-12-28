package org.forum.main.entities;

import jakarta.persistence.*;
import lombok.*;
import org.forum.auxiliary.constants.DateTimeFormatConstants;
import org.forum.main.entities.enums.Gender;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {
        "user"
})
@ToString(exclude = {
        "user"
})
@Entity
@Table(name = "forum_user_details")
public class UserInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    @Column(name = "image")
    private String linkToImage;

    @OneToOne
    @JoinColumn(name = "forum_user_id")
    private User user;

    public void setUser(User user) {
        this.user = user;
        if (user.getUserInformation() == null) {
            user.setUserInformation(this);
        }
    }

    public String getFormattedDateOfBirth() {
        return dateOfBirth.format(DateTimeFormatConstants.DAY_MONTH_YEAR);
    }

}
