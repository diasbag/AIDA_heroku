package com.hackathon.mentor.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
//
//    @Column(name = "phone_number")
//    private String number;


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
    private Integer yearOfGraduation;

    @Column(name = "subject1")
    private String subjectOfInterest1;

    @Column(name = "subject2")
    private String subjectOfInterest2;

    @Column(name = "list_of_skills")
    @ElementCollection
    private List<String> listOfSkills;

    @ManyToMany
    @Column(name = "mentee_id")
    private Set<Mentee> mentees = new HashSet<>();

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "user = " + user + ", " +
                "age = " + age + ", " +
                "iin = " + iin + ", " +
                "bachelorsMajor = " + bachelorsMajor + ", " +
                "mastersMajor = " + mastersMajor + ", " +
                "bachelorsUniversity = " + bachelorsUniversity + ", " +
                "countryOfBachelorsUniversity = " + countryOfBachelorsUniversity + ", " +
                "mastersUniversity = " + mastersUniversity + ", " +
                "countryOfMastersUniversity = " + countryOfMastersUniversity + ", " +
                "country = " + country + ", " +
                "work = " + work + ", " +
                "userInfo = " + userInfo + ", " +
                "school = " + school + ", " +
                "yearOfGraduation = " + yearOfGraduation + ", " +
                "subjectOfInterest1 = " + subjectOfInterest1 + ", " +
                "subjectOfInterest2 = " + subjectOfInterest2 + ", " +
                "listOfSkills = " + listOfSkills + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mentor mentor = (Mentor) o;
        return Objects.equals(id, mentor.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
