package com.hackathon.mentor.payload.request;

import lombok.Data;

@Data
public class RatingRequest {

    private double rate;

    private String comment;

}
