package com.hackathon.mentor.payload.request;

import lombok.*;

import javax.validation.constraints.*;

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

    private String work;

    private Integer yearOfGraduation;

}
