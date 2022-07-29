package com.hackathon.mentor.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne
    @JoinColumn(name = "mentor_id")
    private Mentor mentor;

}
