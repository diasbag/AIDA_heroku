package com.hackathon.mentor.controllers;

import com.hackathon.mentor.models.Mentor;
import com.hackathon.mentor.service.RecommendationsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin(value = "*", maxAge = 3600)
@RequiredArgsConstructor
@RequestMapping("/recommendations")
public class RecommendationsController {
    private final RecommendationsService recommendationsService;

    @GetMapping("/by_subject")
    public List<Mentor> getRecommendations(@RequestParam Long mentorID) {
        return recommendationsService.getRecommendations(mentorID);
    }


}
