package com.hackathon.mentor.payload.response;

import lombok.Getter;
import lombok.Setter;


import com.hackathon.mentor.models.Rating;
@Getter
@Setter
public class MentorResponseSuper {
    private int age;

    private String number;

    private Rating rating;

    private String iin;

    private String bachelorsMajor;
    private String mastersMajor;

    private String bachelorsUniversity;
    private String countryOfBachelorsUniversity;
    private String mastersUniversity;
    private String countryOfMastersUniversity;


    private String country;

    private String work;

    private String userInfo;

    private String school;
    private Integer yearOfGraduation;
    private String subject1;
    private String subject2;
    private int MenteesCount;
}
