package com.hackathon.mentor.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
@Getter
@Setter
public class UpdateMentorRequest {

    private String email;

    private String firstname;
    private String middlename;
    @Size(min = 3, max = 20)
    private String lastname;

    @Min(value = 0, message = "Age < 0")
    @Max(100)
    private int age;

    private String number;

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
    private String firstSubject;
    private String secondSubject;
    private Integer yearOfGraduation;

}
