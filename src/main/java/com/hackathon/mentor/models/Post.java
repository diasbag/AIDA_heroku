package com.hackathon.mentor.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "posts")
@Getter
@Setter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title", length = 10485760, columnDefinition = "TEXT")
    @Size(max =  10485760)
    private String title;
    @Column(name = "created_date")
    private Date date;
    @Column(name = "article", length = 10485760, columnDefinition = "TEXT")
    @Size(max =  10485760)
    private String article;
    @OneToOne(cascade = {CascadeType.ALL})
    private Image image;

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", date=" + date +
                ", article='" + article + '\'' +
                '}';
    }
}
