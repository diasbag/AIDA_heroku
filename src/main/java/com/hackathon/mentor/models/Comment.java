package com.hackathon.mentor.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "comments")
@Getter
@Setter

public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Double communicativeActivity;
    private Double subjectKnowledge;
    private Double dataQuality;
    private Double averageScore;
    private Date dateOfComment;

    @Column(name = "comment")
    private String comment;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;
}
