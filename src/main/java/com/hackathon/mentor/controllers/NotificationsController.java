package com.hackathon.mentor.controllers;

import com.hackathon.mentor.models.SSEEmitter;
import com.hackathon.mentor.models.SerializableSSE;
import com.hackathon.mentor.payload.request.NotificationRequest;
import com.hackathon.mentor.service.EmitterService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(value = "*", maxAge = 3600)
@SecurityRequirement(name = "basicauth")
@RequestMapping("/notification")
public class NotificationsController {

    private final EmitterService emitterService;
    @GetMapping("/subscription")
    public SSEEmitter subscribe() {
        return emitterService.addEmitter();
    }
    @GetMapping("/get_emitter")
    public SSEEmitter getSubscription(@RequestParam Long id) {
        return emitterService.getEmitter(id);
    }
    @PostMapping("/{id}")
    public ResponseEntity<?> sendToRateNotification(@PathVariable Long id, @RequestBody NotificationRequest request) {
        emitterService.sendToRateNotification(id, request.getTopic(), request.getMessage());
        return ResponseEntity.ok().body("message pushed to user " + id);
    }

}
