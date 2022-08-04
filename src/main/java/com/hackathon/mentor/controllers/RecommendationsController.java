package com.hackathon.mentor.controllers;

import com.hackathon.mentor.service.RecommendationsService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(value = "*", maxAge = 3600)
@RequiredArgsConstructor
@RequestMapping("/recommendations")
@SecurityRequirement(name = "basicauth")
public class RecommendationsController {
    private final RecommendationsService recommendationsService;

    @GetMapping()
    public ResponseEntity<?> getRecommendations(@RequestParam Long mentorID) {
        return ResponseEntity.ok(recommendationsService.getRecommendations(mentorID));
    }


}
