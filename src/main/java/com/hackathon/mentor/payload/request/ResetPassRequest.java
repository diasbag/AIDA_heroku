package com.hackathon.mentor.payload.request;

import lombok.Data;

@Data
public class ResetPassRequest {
    private String resetToken;
    private String password;
}
