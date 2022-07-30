package com.hackathon.mentor.payload.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class SignupUpdateMentorRequest extends SignupUpdateMenteeRequest {

    private String bachelorsMajor;
    private String mastersMajor;

    private String bachelorsUniversity;
    private String countryOfBachelorsUniversity;
    private String mastersUniversity;
    private String countryOfMastersUniversity;

    private String countryOfResidence;
    private List<String> listOfSkills;

    private String work;

    private Integer yearOfGraduation;

}
