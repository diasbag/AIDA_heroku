package com.hackathon.mentor.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RatingNotification {
    @Id
    private Long id;
    @OneToOne
    private Mentor mentor;
    @OneToOne
    private Mentee mentee;
}
