package com.hackathon.mentor.controllers;

import com.hackathon.mentor.payload.request.SignupUpdateMenteeRequest;
import com.hackathon.mentor.payload.response.MentorsResponse;
import com.hackathon.mentor.service.MenteeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("mentor/is_my_mentor/{id}")
    public Boolean isMyMentor(@PathVariable("id") Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        return menteeService.isMyMentor(email, id);
    }
    @GetMapping(value = "/my_mentor", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getMyMentor() {
        MentorsResponse mentorsResponse = menteeService.getMyMentor();
        if (mentorsResponse == null) {
            return ResponseEntity.ok("{\"message\":\"Mentee has no mentor\"}");
        } else {
            return ResponseEntity.ok(mentorsResponse);
        }
    }
}
