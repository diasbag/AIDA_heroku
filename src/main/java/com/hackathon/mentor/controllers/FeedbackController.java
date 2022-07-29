package com.hackathon.mentor.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;

@RestController
@CrossOrigin(value = "*", maxAge = 3600)
@RequiredArgsConstructor
@RequestMapping("/feedback")
public class FeedbackController {

    @PostMapping()
    public void leaveFeedback(@RequestParam @Email String email) {

    }
}
