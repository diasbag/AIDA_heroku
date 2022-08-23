package com.hackathon.mentor.controllers;

import com.hackathon.mentor.payload.response.MentorsResponse;
import com.hackathon.mentor.service.RecommendationsService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin(value = "*", maxAge = 3600)
@RequiredArgsConstructor
@RequestMapping("/recommendations")
@SecurityRequirement(name = "basicauth")
public class RecommendationsController {
    private final RecommendationsService recommendationsService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getRecommendations(@RequestParam Long mentorID) {
        List<MentorsResponse> out = recommendationsService.getRecommendations(mentorID);
        if (out.isEmpty()) {
            return ResponseEntity.ok("{\"recommendations\": null}");
        } else {
            return ResponseEntity.ok(recommendationsService.getRecommendations(mentorID));
        }
    }
}
