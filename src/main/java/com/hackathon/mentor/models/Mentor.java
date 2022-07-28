package com.hackathon.mentor.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(	name = "mentors")
public class Mentor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private  User user;

    @Min(value = 0, message = "Age < 0")
    @Max(100)
    private int age;

    @Column(name = "phone_number")
    private String number;


    @Column(name = "iin")
    private String iin;

    private String bachelorsMajor;
    private String mastersMajor;

    private String bachelorsUniversity;
    private String countryOfBachelorsUniversity;
    private String mastersUniversity;
    private String countryOfMastersUniversity;

    @Column(name = "country")
    private String country;

    @Column(name = "work")
    private String work;

    @Column(name = "userInfo")
    private String userInfo;

    @Column(name = "school")
    private String school;

    @Column(name = "finish_year")
    private Integer year;

    @Column(name = "subject1")
    private String subject1;



    @Column(name = "subject2")
    private String subject2;

    @ManyToMany
    @Column(name = "mentee_id")
    private Set<Mentee> mentees = new HashSet<>();


}
