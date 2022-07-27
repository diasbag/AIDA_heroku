package com.hackathon.mentor.payload.request;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.*;
import java.util.Set;

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

    private String major;

    private String university;

    private String country;

    private String work;

    private String userInfo;

    private String school;

    private Integer year;

    private String subject1;

    private String subject2;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(min = 4, max = 40)
    private String password;

}
