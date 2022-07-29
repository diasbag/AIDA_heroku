package com.hackathon.mentor.payload.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hackathon.mentor.models.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@ToString

public class MentorProfileResponse extends MentorResponseSuper{

    private Image image;
}
