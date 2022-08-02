package com.hackathon.mentor.payload.request;

import lombok.Data;


@Data
public class RatingRequest {

    private Double communicativeActivity;
    private Double subjectKnowledge;
    private Double dataQuality;

    private String comment;

}
