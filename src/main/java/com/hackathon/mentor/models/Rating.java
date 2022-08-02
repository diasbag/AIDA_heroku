package com.hackathon.mentor.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
    @Column(name = "knowledge_rating")
    @Min(1)
    @Max(5)
    private Integer knowledgeRating;

    @Column(name = "communication_rating")
    @Min(1)
    @Max(5)
    private Integer communicationRating;

    @Column(name = "quality_of_service_rating")
    @Min(1)
    @Max(5)
    private Integer qualityOfServiceRating;

    @Column(name = "peopleCount")
    private long peopleCount = 0;

    @OneToMany
    private List<Comment> comments = new ArrayList<>();


}
