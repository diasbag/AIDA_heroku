package com.hackathon.mentor.controllers;

import com.hackathon.mentor.models.Mentee;
import com.hackathon.mentor.models.Mentor;
import com.hackathon.mentor.models.Subscribe;
import com.hackathon.mentor.models.User;
import com.hackathon.mentor.payload.request.SignupUpdateMenteeRequest;
import com.hackathon.mentor.service.MenteeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
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
public class MenteeController {

    private final MenteeService menteeService;

    @GetMapping("/mentees")
    public ResponseEntity<?> getAllMentees() {
        return menteeService.getAllMentees();
    }

    @GetMapping("/mentees/{id}")
    public ResponseEntity<?> getMenteeById(@PathVariable("id") Long id) {
        return menteeService.getMenteeById(id);
    }


    @PutMapping("/mentee/profile/edit")
    public ResponseEntity<?>  editProfile(@RequestBody SignupUpdateMenteeRequest updateMenteeRequest) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        return menteeService.editProfile(email, updateMenteeRequest);
    }

    @PostMapping("/mentor/{id}/delete")
    public ResponseEntity<?> deleteMentor(@PathVariable("id") Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        return menteeService.deleteMentor(id, email);
    }

    @GetMapping("/mentee/waitlist")
    public ResponseEntity<?> getSubscribeList() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        return menteeService.getWaitList(email);
    }

    @GetMapping("mentor/isSubscriber/{id}")
    public Boolean isSubscribe(@PathVariable("id") Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        return menteeService.isSubscribe(email, id);
    }
}
