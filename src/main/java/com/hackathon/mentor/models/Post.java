package com.hackathon.mentor.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String title;


    private Date date;

    @NotNull
    private String article;

    @ManyToOne
    @JoinColumn(name = "mentor_id")
    private Mentor mentor;


//    @Lob
//    @Column(name = "image")
//    private byte[] image;

//    public byte[] getPhoto() {
//        return image;
//    }
//
//    public void setPhoto(byte[] photo) {
//        this.image = photo;
//    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public Mentor getMentor() {
        return mentor;
    }

    public void setMentor(Mentor mentor) {
        this.mentor = mentor;
    }
}
