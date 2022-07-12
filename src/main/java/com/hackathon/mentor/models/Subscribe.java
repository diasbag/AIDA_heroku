package com.hackathon.mentor.models;

import javax.persistence.*;
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


    @OneToMany
    @JoinTable(	name = "subscribers",
            joinColumns = @JoinColumn(name = "mentor_id"),
            inverseJoinColumns = @JoinColumn(name = "mentee_id"))
    private List<Mentee> mentee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Mentee getMentee() {
        for (int i = 0; i< mentee.size(); i++) {
            return mentee.get(i);
        }
        return null;
    }

    public List<Mentee> getAllMentees() {
        return mentee;
    }

    public void setMentee(List<Mentee> mentee) {
        this.mentee = mentee;
    }


    public Mentor getMentor() {
        return mentor;
    }

    public void setMentor(Mentor mentor) {
        this.mentor = mentor;
    }
}
