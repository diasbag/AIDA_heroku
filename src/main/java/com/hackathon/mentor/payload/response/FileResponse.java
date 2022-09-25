package com.hackathon.mentor.payload.response;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter

public class FileResponse extends MentorResponseSuper{

    private String id;
    private String name;
    private Long size;
    private String url;
    private String contentType;

}
