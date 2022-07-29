package com.hackathon.mentor.controllers;

import com.hackathon.mentor.payload.request.SignupUpdateMentorRequest;
import com.hackathon.mentor.service.serviceImpl.MentorServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@SecurityRequirement(name = "basicauth")
@RequestMapping("/api")
@RequiredArgsConstructor
public class MentorController {

    private final PasswordEncoder encoder;

    private final MentorServiceImpl mentorService;

    @GetMapping("/mentors")
    public ResponseEntity<?> getMentors() {
        return mentorService.getMentors();
    }

//    @PostMapping("/mentors/filter")
//    public ResponseEntity<?> getMentorsByCountry(@RequestBody FilterRequest filterRequest) {
//        String country = filterRequest.getCountry();
//        String major = filterRequest.getMajor();
//        String university = filterRequest.getUniversity();
//        return mentorService.filtration(country, major, university);
//    }

    @GetMapping("/mentors/{id}")
    public ResponseEntity<?> getMentorById(@PathVariable("id") Long id) {
        return mentorService.getMentorById(id);
    }

    @GetMapping("/user/profile")
    public ResponseEntity<?> getProfile() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        return mentorService.getProfile(email);
    }

    @PutMapping("/mentor/profile/edit")
    public ResponseEntity<?> updateMentor(@RequestBody SignupUpdateMentorRequest signupMentorRequest) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        return mentorService.updateMentor(email, signupMentorRequest);
    }

    @GetMapping("/mentor/subscribers")
    public ResponseEntity<?> getMySubscribers() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        return mentorService.getMySubscribers(email);
    }

    @PutMapping("/mentor/mentee/{id}/confirm")
    public ResponseEntity<?> confirm(@PathVariable("id") Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        return mentorService.confirm(id, email);
    }

    @PostMapping("/mentor/mentee/{id}/reject")
    public ResponseEntity<?> reject(@PathVariable("id") Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        return mentorService.reject(id, email);
    }

    @GetMapping("/mentor/mentees")
    public ResponseEntity<?> getMentorMentees() {
       UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       String email = userDetails.getUsername();
       return mentorService.getMentorMentees(email);
    }

    @PostMapping("/mentee/{id}/delete")
    public ResponseEntity<?> deleteFollower(@PathVariable("id") Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        return mentorService.deleteFollower(id, email);
    }

}
