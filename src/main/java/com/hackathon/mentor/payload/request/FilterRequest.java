package com.hackathon.mentor.payload.request;

import lombok.Data;

@Data
public class FilterRequest {
    private String country;
    private String major;
    private String university;
}
