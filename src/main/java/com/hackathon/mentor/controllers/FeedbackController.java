package com.hackathon.mentor.controllers;

import com.hackathon.mentor.payload.request.FeedbackRequest;
import com.hackathon.mentor.service.FeedbackService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@RestController
@CrossOrigin(value = "*", maxAge = 3600)
@RequiredArgsConstructor
@RequestMapping("/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;
    @PostMapping()
    public void leaveFeedback(@RequestBody FeedbackRequest feedbackRequest)
            throws MessagingException, UnsupportedEncodingException {
        feedbackService.createFeedback(feedbackRequest);
    }
}
