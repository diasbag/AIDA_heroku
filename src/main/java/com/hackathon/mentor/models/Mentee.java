package com.hackathon.mentor.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "mentees")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Mentee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private  User user;

    @NotBlank
    @NotNull
    private String number;

    @NotNull
    private String iin;

    @Column(name = "grade")
    private int grade;

    @Column(name = "school")
    private String school;

    @Column(name = "subject1")
    private String subject1;

    @Column(name = "subject2")
    private String subject2;

    @Column(name = "achievements")
    private String achievements;
}
