package com.hackathon.mentor.payload.response;

import com.hackathon.mentor.payload.request.SignupUpdateMentorRequest;
import lombok.Getter;
import lombok.Setter;


import com.hackathon.mentor.models.Rating;
@Getter
@Setter
public class MentorResponseSuper extends SignupUpdateMentorRequest {
    private Rating rating;
    private int MenteesCount;
}
