package com.hackathon.mentor.controllers;


import com.hackathon.mentor.payload.request.RatingRequest;
import com.hackathon.mentor.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;
    @PostMapping("/user/{id}/rate")
    public ResponseEntity<?> rateMentor(@PathVariable("id") Long id, @RequestBody RatingRequest ratingRequest) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        return ratingService.rateUser(id, email, ratingRequest);
    }
    @GetMapping("/who_to_rate")
    public ResponseEntity<?> whoToRate() {
        List<Long> whoToRate = ratingService.whoToRate();
        return ResponseEntity.ok("{\"toRate\":\"" + whoToRate + "\"}");
    }

}
