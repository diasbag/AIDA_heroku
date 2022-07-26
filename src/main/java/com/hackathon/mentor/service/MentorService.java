package com.hackathon.mentor.service;

import com.hackathon.mentor.models.User;
import org.springframework.http.ResponseEntity;

public interface MentorService {
    ResponseEntity<?> rateMentor(Long id, double rate);

    ResponseEntity<?> getUserProfile(User user);

    ResponseEntity<?> filtration(String country, String major, String university);
}
