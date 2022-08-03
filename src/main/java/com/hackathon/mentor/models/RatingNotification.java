package com.hackathon.mentor.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.Date;

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
}
