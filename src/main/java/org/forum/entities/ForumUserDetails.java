package org.forum.entities;

import jakarta.persistence.*;
import org.forum.entities.enums.Gender;

import java.time.LocalDate;

@Entity
@Table(name = "forum_user_details")
public class ForumUserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "gender")
    private Gender gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

}
