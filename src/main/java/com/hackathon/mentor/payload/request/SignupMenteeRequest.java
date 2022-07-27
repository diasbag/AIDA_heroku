package com.hackathon.mentor.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.util.Set;
@Getter
@Setter
public class SignupMenteeRequest {
    @NotBlank
    @Size(min = 1, max = 20)
    @NotNull
    private String firstname;
    @NotBlank
    @NotNull
    @Size(min = 1, max = 20)
    private String lastname;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(min = 4, max = 40)
    private String password;

    @NotNull
    @NotBlank
    private String number;

    @NotNull
    @NotBlank
    private String iin;

    @NotNull
    @NotBlank
    private String school;

    private int grade;

    private String achievements;
    private String subjectOfInterest1;
    private String subjectOfInterest2;

}
