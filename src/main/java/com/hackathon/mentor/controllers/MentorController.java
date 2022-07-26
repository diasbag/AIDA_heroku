package com.hackathon.mentor.controllers;

import com.hackathon.mentor.models.*;
import com.hackathon.mentor.payload.request.FilterRequest;
import com.hackathon.mentor.payload.request.UpdateMentorRequest;
import com.hackathon.mentor.payload.response.MentorProfileResponse;
import com.hackathon.mentor.payload.response.MentorsResponse;
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

import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@SecurityRequirement(name = "basicauth")
@RequestMapping("/api")
public class MentorController {

    @Autowired
    MentorRepository mentorRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    SubscribeRepository subscribeRepository;

    @Autowired
    MenteeRepository menteeRepository;

    @Autowired
    MentorServiceImpl mentorService;

    @Autowired
    PostRepository postRepository;

    @GetMapping("/mentors")
    public ResponseEntity<?> getMentors() {
        List<Mentor> mentors = mentorRepository.getAll();
        List<MentorsResponse> mentorsResponseList = new ArrayList<>();
        for (int i = 0; i < mentors.size(); i++) {
            MentorsResponse mentorsResponse = new MentorsResponse();
            mentorsResponse.setUser(mentors.get(i).getUser());
            mentorsResponse.setAge(mentors.get(i).getAge());
            mentorsResponse.setCountry(mentors.get(i).getCountry());
            mentorsResponse.setRating(mentors.get(i).getRating());
            mentorsResponse.setIin(mentors.get(i).getIin());
            mentorsResponse.setMajor(mentors.get(i).getMajor());
            mentorsResponse.setNumber(mentors.get(i).getNumber());
            mentorsResponse.setSchool(mentors.get(i).getSchool());
            mentorsResponse.setUniversity(mentors.get(i).getUniversity());
            mentorsResponse.setUserInfo(mentors.get(i).getUserInfo());
            mentorsResponse.setWork(mentors.get(i).getWork());
            mentorsResponseList.add(mentorsResponse);
            mentorsResponse.setMenteesCount(mentors.get(i).getMentees().size());
        }
        return new ResponseEntity<>(mentorsResponseList, HttpStatus.OK);
    }

//    @GetMapping("mentors/filter/{country}/{major}")
//    public ResponseEntity<?> filtration(@PathVariable(value = "country", required = false) String country, @PathVariable(value = "major", required = false) String major) {
//        return mentorService.filtration(country, major);
//    }
    @PostMapping("mentors/filter")
    public ResponseEntity<?> getMentorsByCountry(@RequestBody FilterRequest filterRequest) {
        String country = filterRequest.getCountry();
        String major = filterRequest.getMajor();
        String university = filterRequest.getUniversity();
        return mentorService.filtration(country, major, university);
////        List<Mentor> mentors = null;
////        if (country != null && major != null) {
////            mentors = mentorRepository.findByCountryAndMajor(country , major);
////        } else if (country != null && major == null) {
////            mentors = mentorRepository.findByCountry(country);
////        } else if (country == null && major != null) {
////            mentors = mentorRepository.findByMajor(major);
////        }
////        List<MentorsResponse> mentorsResponseList = new ArrayList<>();
////        if (mentors == null) {
////            return new ResponseEntity<>("Not found", HttpStatus.NOT_FOUND);
////        }
////        for (int i = 0; i < mentors.size(); i++) {
////            MentorsResponse mentorsResponse = new MentorsResponse();
////            mentorsResponse.setUser(mentors.get(i).getUser());
////            mentorsResponse.setAge(mentors.get(i).getAge());
////            mentorsResponse.setCountry(mentors.get(i).getCountry());
////            mentorsResponse.setRating(mentors.get(i).getRating());
////            mentorsResponse.setIin(mentors.get(i).getIin());
////            mentorsResponse.setMajor(mentors.get(i).getMajor());
////            mentorsResponse.setNumber(mentors.get(i).getNumber());
////            mentorsResponse.setSchool(mentors.get(i).getSchool());
////            mentorsResponse.setUniversity(mentors.get(i).getUniversity());
////            mentorsResponse.setUserInfo(mentors.get(i).getUserInfo());
////            mentorsResponse.setWork(mentors.get(i).getWork());
////            mentorsResponseList.add(mentorsResponse);
////        }
////        return new ResponseEntity<>(mentorsResponseList, HttpStatus.OK);
    }

    @GetMapping("/mentors/{id}")
    public ResponseEntity<?> getMentorById(@PathVariable("id") Long id) {
        Mentor mentor = mentorRepository.findById(id).orElse(null);
        if (mentor == null) {
            return new ResponseEntity<>("Not Found!!!", HttpStatus.NOT_FOUND);
        }
        MentorProfileResponse mentorProfileResponse = new MentorProfileResponse();
        List<Post> posts = postRepository.getByMentor(mentor);
        mentorProfileResponse.setFirstname(mentor.getUser().getFirstname());
        mentorProfileResponse.setLastname(mentor.getUser().getLastname());
        mentorProfileResponse.setEmail(mentor.getUser().getEmail());
        mentorProfileResponse.setImage(mentor.getUser().getImage());
        mentorProfileResponse.setAge(mentor.getAge());
        mentorProfileResponse.setIin(mentor.getIin());
        mentorProfileResponse.setRating(mentor.getRating());
        mentorProfileResponse.setMenteesCount(mentor.getMentees().size());
        mentorProfileResponse.setNumber(mentor.getNumber());
        mentorProfileResponse.setMajor(mentor.getMajor());
        mentorProfileResponse.setCountry(mentor.getCountry());
        mentorProfileResponse.setSchool(mentor.getSchool());
        mentorProfileResponse.setPosts(posts);
        mentorProfileResponse.setWork(mentor.getWork());
        mentorProfileResponse.setUserInfo(mentor.getUserInfo());
        mentorProfileResponse.setUniversity(mentor.getUniversity());

        return new ResponseEntity<>(mentorProfileResponse, HttpStatus.OK);
    }

    @GetMapping("/user/profile")
    public ResponseEntity<?> getProfile() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email).orElse(null);
        ERole role = user.getRoles().get(0).getName();
        if (role.name().equals("ROLE_MENTOR") ) {
            Mentor mentor = mentorRepository.findByUser(user);
            List<Post> posts = postRepository.getByMentor(mentor);
//            ActiveMentorProfileDTO mentorDTO = new ActiveMentorProfileDTO();
//            mentorDTO.setFirstname(user.getFirstname());
//            mentorDTO.setLastname(user.getLastname());
//            mentorDTO.setEmail(user.getEmail());
//            mentorDTO.setImage(user.getImage());
//            mentorDTO.setMajor(mentor.getMajor());
//            mentorDTO.setAge(mentor.getAge());
//            mentorDTO.setCountry(mentor.getCountry());
//            mentorDTO.setPosts(posts);
//            mentorDTO.setRating(mentor.getRating().getRating());
//            mentorDTO.setVotes(mentor.getRating().getPeopleCount());
//            mentorDTO.setPostCount(posts.size());
//            mentorDTO.setMentees(mentor.getMentees());
//            mentorDTO.setMenteesCount(mentor.getMentees().size());
//            mentorDTO.setNumber(mentor.getNumber());
//            mentorDTO.setSchool(mentor.getSchool());
//            mentorDTO.setUniversity(mentor.getUniversity());
//            mentorDTO.setUserInfo(mentor.getUserInfo());
            return new ResponseEntity<>(mentor, HttpStatus.OK);
        }
       if (role.equals(ERole.ROLE_MENTEE)) {
           Mentee mentee = menteeRepository.findByUser(user);
//           ActiveMenteeProfileDTO menteeDTO = new ActiveMenteeProfileDTO();
//           menteeDTO.setFirstname(mentee.getUser().getFirstname());
//           menteeDTO.setLastname(mentee.getUser().getLastname());
//           menteeDTO.setEmail(mentee.getUser().getEmail());
//           menteeDTO.setImage(mentee.getUser().getImage());
//           menteeDTO.setGrade(mentee.getGrade());
//           menteeDTO.setAchievements(mentee.getAchievements());
//           menteeDTO.setNumber(mentee.getNumber());
//           menteeDTO.setSchool(mentee.getSchool());
           return new ResponseEntity<>(mentee, HttpStatus.OK);
       }
        return null;
    }
    @PutMapping("/mentor/profile/edit")
    public ResponseEntity<?> updateMentor(@RequestBody  UpdateMentorRequest signupMentorRequest) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        User user = userRepository.getByEmail(email);
        Mentor mentor = mentorRepository.findByUser(user);

        user.setEmail(signupMentorRequest.getEmail());
        user.setFirstname(signupMentorRequest.getFirstname());
        user.setLastname(signupMentorRequest.getLastname());

        userRepository.save(user);

        mentor.setAge(signupMentorRequest.getAge());
        mentor.setIin(signupMentorRequest.getIin());
        mentor.setMajor(signupMentorRequest.getMajor());
        mentor.setUniversity(signupMentorRequest.getUniversity());
        mentor.setCountry(signupMentorRequest.getCountry());
        mentor.setNumber(signupMentorRequest.getNumber());
        mentor.setWork(signupMentorRequest.getWork());
        mentor.setUserInfo(signupMentorRequest.getUserInfo());
        mentor.setSchool(signupMentorRequest.getSchool());
        mentorRepository.save(mentor);
        return ResponseEntity.ok("User updated successfully!");
    }

    @GetMapping("/mentor/subscribers")
    public ResponseEntity<?> getMySubscribers() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();

        User user = userRepository.findByEmail(email).orElse(null);
        Mentor mentor = mentorRepository.findByUser(user);

        List<Subscribe> subscribes = subscribeRepository.findByMentor(mentor);

        List<Mentee> mentees = new ArrayList<>();
        for(int i = 0; i < subscribes.size(); i++) {
            mentees.add(subscribes.get(i).getMentee());
        }
        return new ResponseEntity<>(mentees, HttpStatus.OK);
    }

    @PutMapping("/mentor/mentee/{id}/confirm")
    public ResponseEntity<?> confirm(@PathVariable("id") Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email).orElse(null);
        Mentor mentor = mentorRepository.findByUser(user);
        Mentee mentee = menteeRepository.findById(id).orElse(null);
        Subscribe subscribe = subscribeRepository.getByMentorAndMentee(mentor, mentee);
        mentor.getMentees().add(mentee);
        mentorRepository.save(mentor);
        Long sid = subscribe.getId();
        subscribeRepository.deleteById(sid);
        return new ResponseEntity<>("Success!!!", HttpStatus.OK);
    }

    @PostMapping("/mentor/mentee/{id}/reject")
    public ResponseEntity<?> reject(@PathVariable("id") Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email).orElse(null);
        Mentor mentor = mentorRepository.findByUser(user);
        Mentee mentee = menteeRepository.findById(id).orElse(null);
        Subscribe subscribe = subscribeRepository.getByMentorAndMentee(mentor, mentee);
        Long sid = subscribe.getId();
        System.out.println("qwerqwerqwerqwerqwer   " + subscribe.getId());
        subscribeRepository.deleteById(sid);

        return new ResponseEntity<>("Success" , HttpStatus.OK);
    }

    @GetMapping("/mentor/mentees")
    public ResponseEntity<?> getMentorMentees() {
       UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       String email = userDetails.getUsername();
       User user = userRepository.findByEmail(email).orElse(null);
       Mentor mentor = mentorRepository.findByUser(user);


       return new ResponseEntity<>(mentor.getMentees(), HttpStatus.OK);
    }

    @PostMapping("/mentor/mentees/{id}/delete")
    public ResponseEntity<?> deleteFollower(@PathVariable("id") Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email).orElse(null);
        Mentor mentor = mentorRepository.findByUser(user);

        Mentee mentee = menteeRepository.findById(id).orElseThrow(() -> new RuntimeException("Mentee Not Found!!!!"));

        mentor.getMentees().remove(mentee);

        mentorRepository.save(mentor);
        subscribeRepository.deleteByMentorAndMentee(mentor, mentee);

        return new ResponseEntity<>(mentor, HttpStatus.OK);
    }

}
