package com.hackathon.mentor.controllers;

import com.hackathon.mentor.models.Mentee;
import com.hackathon.mentor.models.Mentor;
import com.hackathon.mentor.models.Subscribe;
import com.hackathon.mentor.models.User;
import com.hackathon.mentor.repository.MenteeRepository;
import com.hackathon.mentor.repository.MentorRepository;
import com.hackathon.mentor.repository.SubscribeRepository;
import com.hackathon.mentor.repository.UserRepository;
import com.hackathon.mentor.security.services.MentorService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@SecurityRequirement(name = "basicauth")
@RequestMapping("/api")
public class SubscribeController {

    @Autowired
    SubscribeRepository subscribeRepository;

    @Autowired
    MenteeRepository menteeRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MentorRepository mentorRepository;

    @GetMapping("/subscribes")
    public ResponseEntity<?> getSubscribes() {
        List<Subscribe> subscribeList = subscribeRepository.findAll();
        return new ResponseEntity<>(subscribeList , HttpStatus.OK);
    }

    @PostMapping("/mentor/{id}/subscribe")
    public ResponseEntity<?> subscribe(@PathVariable("id") Long id) {
        UserDetails userDetails = (UserDetails)  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email).orElse(null);
        Mentee mentee = menteeRepository.findByUser(user);
        Mentor mentor = mentorRepository.findById(id).orElse(null);
        Subscribe subscribe = new Subscribe();
        List<Mentee> mentees = new ArrayList<>();
        mentees.add(mentee);
        subscribe.setMentee(mentees);
        subscribe.setMentor(mentor);
        subscribeRepository.save(subscribe);

        return new ResponseEntity<>("Success!!!", HttpStatus.OK);

    }
}
