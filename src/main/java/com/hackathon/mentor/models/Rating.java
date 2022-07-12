package com.hackathon.mentor.models;

import javax.persistence.*;

@Entity
@Table(name = "ratings")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double rating = 0;

    private long peopleCount = 0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public long getPeopleCount() {
        return peopleCount;
    }

    public void setPeopleCount(long peopleCount) {
        this.peopleCount = peopleCount;
    }
}
