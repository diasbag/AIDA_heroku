package com.hackathon.mentor.payload.request;

import com.hackathon.mentor.models.Image;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.util.Date;
@Getter
@Setter
@ToString
public class PostRequest {
    @NotNull
    private Long id;


    @NotNull
    private String title;


    @NotNull
    private String article;

}
