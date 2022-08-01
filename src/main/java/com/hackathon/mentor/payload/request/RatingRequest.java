package com.hackathon.mentor.payload.request;

import lombok.Data;


@Data
public class RatingRequest {

    private Integer knowledgeRating;
    private Integer communicationRating;
    private Integer qualityOfServiceRating;

    private String comment;

}
