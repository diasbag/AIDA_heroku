package com.hackathon.mentor.payload.request;

import lombok.Data;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.*;
import java.util.Set;
@Getter
@Setter
@Data
public class SignupMentorRequest {
    @NotBlank
    @Size(min = 1, max = 20)
    private String firstname;
    @NotBlank
    @Size(min = 1, max = 20)
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

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(min = 4, max = 40)
    private String password;

}
