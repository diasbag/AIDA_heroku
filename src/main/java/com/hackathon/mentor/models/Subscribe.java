package com.hackathon.mentor.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "subscribes")
public class Subscribe {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Mentor mentor;


    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Mentee> mentee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Mentee getMentee() {
        List<Mentee> mentees = new ArrayList<>(mentee);
        for (Mentee value : mentees) {
            return value;
        }
        return null;
    }

    public Set<Mentee> getAllMentees() {
        return mentee;
    }

    public void setMentee(Set<Mentee> mentee) {
        this.mentee = mentee;
    }


    public Mentor getMentor() {
        return mentor;
    }

    public void setMentor(Mentor mentor) {
        this.mentor = mentor;
    }
}
