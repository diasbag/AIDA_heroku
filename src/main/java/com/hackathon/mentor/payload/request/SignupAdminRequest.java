package com.hackathon.mentor.payload.request;

import lombok.*;

import javax.validation.constraints.*;

@Builder
@Data
public class SignupAdminRequest {
    @NotBlank
    @Size(min = 1, max = 20)
    private String firstname;
    @NotBlank
    @Size(min = 1, max = 20)
    private String lastname;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(min = 4, max = 40)
    private char[] password;


}
