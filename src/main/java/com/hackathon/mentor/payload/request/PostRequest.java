package com.hackathon.mentor.payload.request;

import com.hackathon.mentor.models.Image;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class PostRequest {


    @NotNull
    private String title;


    @NotNull
    private String article;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }
}
