package com.hackathon.mentor.service;

import com.hackathon.mentor.models.Mentor;
import com.hackathon.mentor.models.Skills;
import com.hackathon.mentor.models.User;
import com.hackathon.mentor.payload.request.FilterRequest;
import com.hackathon.mentor.payload.request.SignupUpdateMentorRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MentorService {

    ResponseEntity<?> getUserProfile(User user);

    ResponseEntity<?> getMentors(Integer page);

    ResponseEntity<?> filtration(FilterRequest request, Integer page);

    ResponseEntity<?> getMentorById(Long id);

    ResponseEntity<?> getProfile(String email);

    ResponseEntity<?> updateMentor(String email, SignupUpdateMentorRequest updateMentorRequest);

    ResponseEntity<?> getMySubscribers(String email);

    ResponseEntity<?> getMentorByUniversity(String name);

    ResponseEntity<?> confirm(Long id, String email);

    ResponseEntity<?> reject(Long id, String email);

    ResponseEntity<?> getMentorMentees(String email);

    ResponseEntity<?> deleteFollower(Long id, String email);
}
