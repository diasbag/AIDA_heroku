package com.hackathon.mentor.controllers;

import com.hackathon.mentor.models.SSEEmitter;
import com.hackathon.mentor.models.SerializableSSE;
import com.hackathon.mentor.payload.request.NotificationRequest;
import com.hackathon.mentor.payload.request.PostRequest;
import com.hackathon.mentor.service.EmitterService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(value = "*", maxAge = 3600)
@SecurityRequirement(name = "basicauth")
@RequestMapping("/notification")
public class NotificationsController {

    private final EmitterService emitterService;
    @GetMapping("/subscription")
    public ResponseEntity<?> subscribe() {
        return ResponseEntity.ok("subscribed");
    }
    @GetMapping("/get_emitter")
    public SSEEmitter getSubscription(@RequestParam Long id) {
        return emitterService.getEmitter(id);
    }

    @PostMapping("/send_to_rate")
    public ResponseEntity<?> sendToRateNotification(@RequestParam Long raterId, Long toRateID) {
        emitterService.sendToRateNotification(raterId, toRateID);
        return ResponseEntity.ok().body("message pushed to user " + toRateID);
    }

    @PostMapping("/send_news/{id}")
    public ResponseEntity<?> sendNews(@PathVariable Long id) {
        emitterService.sendNews(id);
        return ResponseEntity.ok().body("news pushed to users - " + id);
    }
    @PostMapping("/subscription/{id}")
    public ResponseEntity<?> sendSubscriptionNotification(@PathVariable Long id) {
        emitterService.sendSubscriptionNotification(id);
        return ResponseEntity.ok().body("subscription notification was pushed to user - " + id);
    }
}
