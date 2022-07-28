package com.hackathon.mentor.service;

import com.hackathon.mentor.payload.request.SignupUpdateMenteeRequest;
import com.hackathon.mentor.payload.request.SignupUpdateMentorRequest;
import com.hackathon.mentor.payload.response.MessageResponse;

public interface RegistrationService {
    MessageResponse regMentor(SignupUpdateMentorRequest signupUpdateMentorRequest);

    MessageResponse regMentee(SignupUpdateMenteeRequest signupUpdateMenteeRequest);
}
