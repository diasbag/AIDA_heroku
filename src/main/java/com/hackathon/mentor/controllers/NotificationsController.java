package com.hackathon.mentor.controllers;

import com.hackathon.mentor.security.jwt.JwtUtils;
import com.hackathon.mentor.service.EmitterService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@CrossOrigin(value = "*", maxAge = 3600)
@SecurityRequirement(name = "basicauth")
@RequestMapping("/notification")
public class NotificationsController {

    private final EmitterService emitterService;
    private final JwtUtils jwtUtils;

    @GetMapping(value = "/subscription",headers = "Accept=*/*", consumes = MediaType.ALL_VALUE,
            produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseBodyEmitter subscribe(@RequestParam String token, HttpServletResponse httpServletResponse) {
        httpServletResponse.addHeader("charset","UTF-8");
        String email =  jwtUtils.getUserNameFromJwtToken(token);
        return emitterService.addEmitter(email);
    }

    @PostMapping("/send_to_rate")
    public ResponseEntity<?> sendToRateNotification(@RequestParam Long raterId, Long toRateID) {
        emitterService.sendToRateNotification(raterId, toRateID, true);
        return ResponseEntity.ok().body("message pushed to user " + toRateID);
    }


    @PostMapping("/send_news/{newsID}")
    public ResponseEntity<?> sendNews(@PathVariable Long newsID) {
        emitterService.sendNews(newsID);
        return ResponseEntity.ok().body("news pushed to users - " + newsID);
    }
    @PostMapping("/subscription")
    public ResponseEntity<?> sendSubscriptionNotification(@RequestParam Long mentorID, Long menteeID) {
        emitterService.sendSubscriptionNotification(mentorID, menteeID);
        return ResponseEntity.ok().body("subscription notification was pushed to user - " + mentorID);
    }

    @PostMapping("/confirmation")
    public ResponseEntity<?> sendConfirmSubscriptionNotification(@RequestParam Long mentorID, Long menteeID) {
        emitterService.confirmationNotification(mentorID, menteeID);
        return ResponseEntity.ok().body("subscription notification was pushed to user - " + menteeID);
    }
}
