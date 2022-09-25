package com.hackathon.mentor.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "posts")
@Getter
@Setter

public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title_rus", length = 10485760, columnDefinition = "TEXT")
    @Size(max =  1024)
    private String titleRus;

    @Column(name = "title_kaz", length = 10485760, columnDefinition = "TEXT")
    @Size(max =  1024)
    private String titleKaz;
    @Column(name = "title_eng", length = 10485760, columnDefinition = "TEXT")
    @Size(max =  1024)
    private String titleEng;

    @Column(name = "created_date")
    private Date date;
    @Column(name = "article_rus", length = 10485760, columnDefinition = "TEXT")
    @Size(max =  10485760)
    private String articleRus;

    @Column(name = "article_kaz", length = 10485760, columnDefinition = "TEXT")
    @Size(max =  10485760)
    private String articleKaz;

    @Column(name = "article_eng", length = 10485760, columnDefinition = "TEXT")
    @Size(max =  10485760)
    private String articleEng;

    @Column(name = "url", length = 10485760, columnDefinition = "TEXT")
    @Size(max =  10485760)
    private String URL;
    @OneToOne(cascade = {CascadeType.ALL})
    private Image image;
    @OneToOne
    @JsonIgnore
    private User user;

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + titleRus + '\'' +
                ", date=" + date +
                ", article='" + articleRus + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(id, post.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
