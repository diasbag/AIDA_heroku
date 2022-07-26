package com.hackathon.mentor.controllers;

import com.hackathon.mentor.models.*;
import com.hackathon.mentor.payload.request.RatingRequest;
import com.hackathon.mentor.payload.request.UpdateMenteeRequest;
import com.hackathon.mentor.repository.*;
import com.hackathon.mentor.service.MentorServiceImpl;
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
    MentorServiceImpl mentorService;

    @Autowired
    RatingRepository ratingRepository;


    @GetMapping("/mentees")
    public ResponseEntity<?> getAllMentees() {
        List<Mentee> mentees = menteeRepository.getAll();
        return new ResponseEntity<>(mentees, HttpStatus.OK);
    }

    @GetMapping("/mentees/{id}")
    public ResponseEntity<?> getMenteeById(@PathVariable("id") Long id) {
        Mentee mentee = menteeRepository.findById(id).orElse(null);
        if (mentee == null) {
            return new ResponseEntity<>("Not Found!!!", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(mentee, HttpStatus.OK);
    }

//    @GetMapping("/user/profile")
//    public ResponseEntity<?> getMenteeProfile() {
//        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String email = userDetails.getUsername();
//        User user = userRepository.getByEmail(email);
//        Mentee mentee = menteeRepository.findByUser(user);
//
//        return new ResponseEntity<>(mentee, HttpStatus.OK);
//    }

    @PutMapping("/mentee/profile/edit")
    public ResponseEntity<?>  editProfile(@RequestBody UpdateMenteeRequest updateMenteeRequest) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        User user = userRepository.getByEmail(email);
        Mentee mentee = menteeRepository.findByUser(user);

        user.setFirstname(updateMenteeRequest.getFirstname());
        user.setLastname(updateMenteeRequest.getLastname());
        user.setEmail(updateMenteeRequest.getEmail());

        userRepository.save(user);

        mentee.setSchool(updateMenteeRequest.getSchool());
        mentee.setAchievements(updateMenteeRequest.getAchievements());
        mentee.setGrade(updateMenteeRequest.getGrade());
        mentee.setIin(updateMenteeRequest.getIin());
        mentee.setNumber(updateMenteeRequest.getNumber());
        menteeRepository.save(mentee);
        return new ResponseEntity<>(mentee, HttpStatus.OK);
    }



    @PostMapping("/mentees/mentors/{id}")
    public ResponseEntity<?> rateMentor(@PathVariable("id") Long id, @RequestBody RatingRequest ratingRequest) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email).orElse(null);
        Mentee mentee = menteeRepository.findByUser(user);

        Mentor mentor = mentorRepository.findById(id).orElse(null);
        Set<Mentee> mentees = mentor.getMentees();
        if (!mentees.contains(mentee)) {
            return new ResponseEntity<>("poshel Nahui!!! ne tvoi mentor", HttpStatus.CONFLICT);
        }
        Rating rating = mentor.getRating();
        if (rating == null) {
            Rating rating1 = new Rating();
            rating1.setRating(ratingRequest.getRate());
            rating1.setPeopleCount(1);
            ratingRepository.save(rating1);
            mentor.setRating(rating1);
            mentorRepository.save(mentor);
            return new ResponseEntity<>(mentor, HttpStatus.OK);
        }
        long cnt =  (rating.getPeopleCount()+1);
        double res = (rating.getRating()+ ratingRequest.getRate())/(cnt);
        rating.setRating(res);
        rating.setPeopleCount(cnt);
        ratingRepository.save(rating);
        mentor.setRating(rating);
        mentorRepository.save(mentor);
        return new ResponseEntity<>(mentor, HttpStatus.OK);
        //return mentorService.rateMentor(id, rate);
    }
}
