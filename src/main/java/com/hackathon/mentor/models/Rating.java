package com.hackathon.mentor.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ratings")
@Getter
@Setter
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rating")
    private double rating = 0;

    @Column(name = "peopleCount")
    private long peopleCount = 0;

    @OneToMany
    private List<Comment> comments = new ArrayList<>();


}
