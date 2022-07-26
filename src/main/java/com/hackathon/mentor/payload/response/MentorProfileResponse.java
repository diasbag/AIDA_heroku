package com.hackathon.mentor.payload.response;

import com.hackathon.mentor.models.*;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Data
public class MentorProfileResponse {

    private String firstname;
    private String lastname;
    private String middlename;
    private String email;
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

    private int menteesCount;

    private List<Post> posts;

    private Image image;



}
