package com.hackathon.mentor.controllers;

import com.hackathon.mentor.models.Mentee;
import com.hackathon.mentor.models.Mentor;
import com.hackathon.mentor.models.Subscribe;
import com.hackathon.mentor.models.User;
import com.hackathon.mentor.repository.MenteeRepository;
import com.hackathon.mentor.repository.MentorRepository;
import com.hackathon.mentor.repository.SubscribeRepository;
import com.hackathon.mentor.repository.UserRepository;
import com.hackathon.mentor.service.SubscribeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@SecurityRequirement(name = "basicauth")
@RequestMapping("/api")
@RequiredArgsConstructor
public class SubscribeController {



    private final SubscribeService subscribeService;

    @GetMapping("/subscribes")
    public ResponseEntity<?> getSubscribers() {
        return subscribeService.getSubscribers();
    }

    @PostMapping("/mentor/{id}/subscribe")
    public ResponseEntity<?> subscribe(@PathVariable("id") Long id) {
        UserDetails userDetails = (UserDetails)  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        return subscribeService.subscribe(id, email);

    }
}
