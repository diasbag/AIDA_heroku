package com.hackathon.mentor.payload.request;

import lombok.Data;


@Data
public class RatingRequest {

    private Double knowledgeRating;
    private Double communicationRating;
    private Double qualityOfServiceRating;

    private String comment;

}
