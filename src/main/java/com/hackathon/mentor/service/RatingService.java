package com.hackathon.mentor.service;

import com.hackathon.mentor.exceptions.AccountConflict;
import com.hackathon.mentor.payload.request.RatingRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RatingService {

    ResponseEntity<?> rateUser(Long id, String email, RatingRequest ratingRequest);

    List<Long> whoToRate() throws AccountConflict;
}
