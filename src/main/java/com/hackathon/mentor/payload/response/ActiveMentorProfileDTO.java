package com.hackathon.mentor.payload.response;

import com.hackathon.mentor.models.Image;
import com.hackathon.mentor.models.Mentee;
import com.hackathon.mentor.models.Post;
import com.hackathon.mentor.models.Rating;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class ActiveMentorProfileDTO {

    private String firstname;


    private String lastname;


    private String email;
    private int age;

    private String number;

//    private Rating rating;

    private double rating;

    private long votes;

    private String major;

    private String university;

    private String country;

    private String work;

    private String userInfo;

    private String school;

    private int menteesCount;

    private List<Post> posts;

    private int postCount;

    private Image image;

    private Set<Mentee> mentees;
}
