package com.hackathon.mentor.payload.request;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PostEditRequest {
    private Long id;
    private String title;
    private String article;
}
