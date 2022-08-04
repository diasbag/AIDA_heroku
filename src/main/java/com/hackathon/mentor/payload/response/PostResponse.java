package com.hackathon.mentor.payload.response;

import com.hackathon.mentor.models.Image;
import com.hackathon.mentor.models.Mentor;
import com.hackathon.mentor.models.User;
import lombok.Data;
import lombok.Getter;

import java.util.Date;
@Data
public class PostResponse {
    private Long id;
    private String title;
    private String article;
    private Date date;
    private Image image;
 //   private User user;
    private String firstname;
    private String lastname;
}
