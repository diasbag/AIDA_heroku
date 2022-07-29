package com.hackathon.mentor.payload.request;

import lombok.Data;

@Data
public class FeedbackRequest {
    private String email;
    private String comment;
}
