package com.hackathon.mentor.service;

import com.hackathon.mentor.payload.request.RatingRequest;
import org.springframework.http.ResponseEntity;

public interface RatingService {



    ResponseEntity<?> rateUser(Long id, String email, RatingRequest ratingRequest);


}
