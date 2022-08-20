package com.hackathon.mentor.service;


import com.hackathon.mentor.models.User;
import com.hackathon.mentor.payload.request.SignupUpdateMentorRequest;
import org.springframework.http.ResponseEntity;


public interface MentorService {

    ResponseEntity<?> getUserProfile(User user);

    ResponseEntity<?> getMentors(Integer page);

    ResponseEntity<?> filtration(String university, String country, String major, Integer page);

    ResponseEntity<?> getMentorById(Long id);

    ResponseEntity<?> getProfile(String email);

    ResponseEntity<?> updateMentor(String email, SignupUpdateMentorRequest updateMentorRequest);

    ResponseEntity<?> getMySubscribers(String email);

    ResponseEntity<?> getMentorByUniversity(String name, Integer page);

    ResponseEntity<?> confirm(Long id, String email);

    ResponseEntity<?> reject(Long id, String email);

    ResponseEntity<?> getMentorMentees(String email);

    ResponseEntity<?> deleteFollower(Long id, String email);

    Boolean isMyMentor(String email, Long id);
}
