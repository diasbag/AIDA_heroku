package com.hackathon.mentor.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class RatingNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    private Mentor mentor;
    @OneToOne
    private Mentee mentee;
    @JsonIgnore
    private Date dateOfStart;
    @JsonIgnore
    private Date dateOfEnd;
    private Boolean mentorRated;
    private Boolean menteeRated;

    public RatingNotification(Mentor mentor, Mentee mentee) {
        this.mentor = mentor;
        this.mentee = mentee;
        this.mentorRated = false;
        this.menteeRated = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RatingNotification that = (RatingNotification) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
