package com.hackathon.mentor.payload.request;

import lombok.*;

import javax.validation.constraints.Max;


@Getter
@Setter
@Builder
public class LibraryTextRequest {
    private String id;
    @Max(1000)
    private String text;
}
