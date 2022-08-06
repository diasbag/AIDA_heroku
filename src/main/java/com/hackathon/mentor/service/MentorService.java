package com.hackathon.mentor.service;

import com.hackathon.mentor.models.User;
import com.hackathon.mentor.payload.request.SignupUpdateMentorRequest;
import org.springframework.http.ResponseEntity;

public interface MentorService {

    ResponseEntity<?> getUserProfile(User user);

    ResponseEntity<?> getMentors(Integer page);
//    ResponseEntity<?> filtration(String country, String major, String university);

    ResponseEntity<?> getMentorById(Long id);

    ResponseEntity<?> getProfile(String email);

    ResponseEntity<?> updateMentor(String email, SignupUpdateMentorRequest updateMentorRequest);

    ResponseEntity<?> getMySubscribers(String email);

    ResponseEntity<?> confirm(Long id, String email);

    ResponseEntity<?> reject(Long id, String email);

    ResponseEntity<?> getMentorMentees(String email);

    ResponseEntity<?> deleteFollower(Long id, String email);
}
