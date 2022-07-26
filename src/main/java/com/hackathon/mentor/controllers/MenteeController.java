package com.hackathon.mentor.controllers;

import com.hackathon.mentor.models.*;
import com.hackathon.mentor.payload.request.RatingRequest;
import com.hackathon.mentor.payload.request.UpdateMenteeRequest;
import com.hackathon.mentor.repository.*;
import com.hackathon.mentor.services.MenteeService;
import com.hackathon.mentor.services.MentorService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@SecurityRequirement(name = "basicauth")
@RequestMapping("/api")
public class MenteeController {

    @Autowired
    MenteeRepository menteeRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MentorRepository mentorRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    MentorService mentorService;

    @Autowired
    MenteeService menteeService;

    @Autowired
    RatingRepository ratingRepository;


    @GetMapping("/mentees")
    public ResponseEntity<?> getAllMentees() {
        return menteeService.getMentees();
    }

    @GetMapping("/mentees/{id}")
    public ResponseEntity<?> getMenteeById(@PathVariable("id") Long id) {
       return menteeService.getMenteeById(id);
    }

    @PutMapping("/mentee/profile/edit")
    public ResponseEntity<?>  editProfile(@RequestBody UpdateMenteeRequest updateMenteeRequest) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        return menteeService.editMenteeProfile(email, updateMenteeRequest);
    }



    @PostMapping("/mentees/mentors/{id}/rate")
    public ResponseEntity<?> rateMentor(@PathVariable("id") Long id, @RequestBody RatingRequest ratingRequest) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        double rate = ratingRequest.getRate();
        return menteeService.rateMentor(id, rate, email);
        //return mentorService.rateMentor(id, rate);
    }
}
