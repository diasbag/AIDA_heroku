package com.hackathon.mentor.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "mentees")
@Getter
@Setter
public class Mentee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private  User user;

    @Min(value = 0, message = "Age < 0")
    @Max(100)
    private int age;

//    @NotBlank
//    @NotNull
//    private String number;

    @NotNull
    private String iin;

    @Column(name = "school")
    private String school;

    @Column(name = "subject1")
    private String subjectOfInterest1;

    @Column(name = "subject2")
    private String subjectOfInterest2;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "mentor_id")
    private Mentor mentor;
    private String userInfo;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mentee mentee = (Mentee) o;
        return Objects.equals(id, mentee.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
