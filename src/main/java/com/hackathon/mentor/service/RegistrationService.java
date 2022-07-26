package com.hackathon.mentor.service;

import com.hackathon.mentor.payload.request.SignupMenteeRequest;
import com.hackathon.mentor.payload.request.SignupMentorRequest;
import com.hackathon.mentor.payload.response.MessageResponse;

public interface RegistrationService {
    MessageResponse regMentor(SignupMentorRequest signupMentorRequest);

    MessageResponse regMentee(SignupMenteeRequest signupMenteeRequest);
}
