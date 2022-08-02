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
    private Double rating = 0D;

    @Column(name = "knowledge_rating")
    @Min(1)
    @Max(5)
    private Double knowledgeRating;

    @Column(name = "communication_rating")
    @Min(1)
    @Max(5)
    private Double communicationRating;

    @Column(name = "quality_of_service_rating")
    @Min(1)
    @Max(5)
    private Double qualityOfServiceRating;

    @Column(name = "peopleCount")
    private long peopleCount = 0;

    @OneToMany
    private List<Comment> comments = new ArrayList<>();

}
