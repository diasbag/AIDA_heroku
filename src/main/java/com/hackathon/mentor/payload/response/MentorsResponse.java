package com.hackathon.mentor.payload.response;

import com.hackathon.mentor.models.Rating;
import com.hackathon.mentor.models.User;
import lombok.Data;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
public class MentorsResponse {

    private Long id;
    private User user;

    private int age;

    private String number;

    private Rating rating;

    private String iin;

    private String major;

    private String university;

    private String country;

    private String work;

    private String userInfo;

    private String school;

    private int MenteesCount;

}
