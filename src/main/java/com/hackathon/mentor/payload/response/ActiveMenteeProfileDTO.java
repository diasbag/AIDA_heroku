package com.hackathon.mentor.payload.response;

import com.hackathon.mentor.models.Image;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

@Data
public class ActiveMenteeProfileDTO {
    private String firstname;

    private String lastname;

    private String email;

    private String number;

    private int grade;

    private String school;

    private String achievements;

    private Image image;

}
