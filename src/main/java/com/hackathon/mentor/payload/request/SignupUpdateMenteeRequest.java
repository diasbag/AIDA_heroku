package com.hackathon.mentor.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
public class SignupUpdateMenteeRequest {
    @NotBlank
    @Size(min = 1, max = 20)
    @NotNull
    private String firstname;
    @NotBlank
    @NotNull
    @Size(min = 1, max = 20)
    private String middlename;
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
    private String telegram;
//    private String number;

    @NotNull
    @NotBlank
    private String iin;


    @NotNull
    @NotBlank
    private String school;
    @NotNull
    @Min(value = 0, message = "Age < 0")
    @Max(100)
    private int age;
    private String userInfo;
    @NotNull
    @NotBlank
    private String subjectOfInterest1;
    @NotNull
    @NotBlank
    private String subjectOfInterest2;

}
