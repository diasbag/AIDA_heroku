package com.hackathon.mentor.payload.request;

import lombok.Data;

@Data
public class ReportRequest {
    private Long id;
    private Long harasserId;
    private String reason;
}
