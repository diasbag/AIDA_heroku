package com.hackathon.mentor.service;

import com.hackathon.mentor.payload.request.ResetPassRequest;
import com.hackathon.mentor.payload.request.SignupUpdateMenteeRequest;
import com.hackathon.mentor.payload.request.SignupUpdateMentorRequest;
import com.hackathon.mentor.payload.response.MessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;

import javax.servlet.http.HttpServletRequest;

public interface RegistrationService {
    MessageResponse regMentor(SignupUpdateMentorRequest signupUpdateMentorRequest);

    MessageResponse regMentee(SignupUpdateMenteeRequest signupUpdateMenteeRequest);


    @Async
    ResponseEntity<?> forgotPassword(String email, HttpServletRequest request);

    @Async
    ResponseEntity<?> resetPassword(ResetPassRequest request);
}
