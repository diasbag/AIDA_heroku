package com.hackathon.mentor.payload.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class TextResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private List<String> text;
    private List<String> filename;
    private List<String> material;

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + id +
                ", \"firstName\":\"" + firstName + '"' +
                ", \"lastName\":\"" + lastName + '"' +
                ", \"text\":\"" + text + '"' +
                ", \"material\":" + material +
                '}';
    }
}
