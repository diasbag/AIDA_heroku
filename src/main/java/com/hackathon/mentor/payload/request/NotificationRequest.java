package com.hackathon.mentor.payload.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationRequest {
    private Long id;
    private String topic;
    private String message;
}
