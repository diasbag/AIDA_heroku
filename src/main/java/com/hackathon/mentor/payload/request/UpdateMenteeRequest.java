package com.hackathon.mentor.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
@Getter
@Setter
public class UpdateMenteeRequest {

    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String number;
    private String iin;
    private String school;
    private int grade;
    private String achievements;
    private String firstSubject;
    private String secondSubject;
}
