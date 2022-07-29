package com.hackathon.mentor.payload.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hackathon.mentor.models.Rating;
import com.hackathon.mentor.models.User;
import lombok.*;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonIgnoreProperties("user")
public class MentorsResponse extends MentorResponseSuper{

    private Long id;
    private User user;

}
