package com.hackathon.mentor.service;

import com.hackathon.mentor.models.Mentor;
import com.hackathon.mentor.payload.request.SignupUpdateMenteeRequest;
import org.springframework.http.ResponseEntity;

public interface MenteeService {

    ResponseEntity<?> getAllMentees();

    ResponseEntity<?> getMenteeById(Long id);

    ResponseEntity<?> editProfile(String email, SignupUpdateMenteeRequest updateMenteeRequest);

    ResponseEntity<?> deleteMentor(Long id, String email);

    ResponseEntity<?> getWaitList(String email);

    Boolean isSubscribe(String email, Long id);

    Mentor getMyMentor();
}
