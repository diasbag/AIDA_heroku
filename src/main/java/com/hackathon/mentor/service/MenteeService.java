package com.hackathon.mentor.service;

import com.hackathon.mentor.payload.request.RatingRequest;
import com.hackathon.mentor.payload.request.UpdateMenteeRequest;
import org.springframework.http.ResponseEntity;

public interface MenteeService {

    ResponseEntity<?> getAllMentees();

    ResponseEntity<?> getMenteeById(Long id);

    ResponseEntity<?> editProfile(String email, UpdateMenteeRequest updateMenteeRequest);

    ResponseEntity<?> deleteMentor(Long id, String email);
}
