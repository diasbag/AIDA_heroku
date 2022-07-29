package com.hackathon.mentor.payload.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hackathon.mentor.models.Image;
import com.hackathon.mentor.payload.request.SignupUpdateMentorRequest;
import lombok.Getter;
import lombok.Setter;


import com.hackathon.mentor.models.Rating;
@Getter
@Setter
@JsonIgnoreProperties({"password", "iin", "telegram", "email"})
public class MentorResponseSuper extends SignupUpdateMentorRequest {
    private Rating rating;
    private int MenteesCount;
    private Image image;
}
