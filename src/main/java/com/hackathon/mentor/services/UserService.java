package com.hackathon.mentor.services;


import com.hackathon.mentor.models.ERole;
import com.hackathon.mentor.models.Mentee;
import com.hackathon.mentor.models.Mentor;
import com.hackathon.mentor.models.User;
import com.hackathon.mentor.payload.request.UpdateMentorRequest;
import com.hackathon.mentor.repository.MenteeRepository;
import com.hackathon.mentor.repository.MentorRepository;
import com.hackathon.mentor.repository.RatingRepository;
import com.hackathon.mentor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private MentorRepository mentorRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired

    private MenteeRepository menteeRepository;

    public ResponseEntity<?> getUserProfile(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        ERole role = user.getRoles().get(0).getName();
        if (role.equals(ERole.ROLE_MENTOR)) {
            Mentor mentor = mentorRepository.findByUser(user);
            // List<Post> posts = postRepository.getByMentor(mentor);
            return new ResponseEntity<>(mentor, HttpStatus.OK);
        }
        if (role.equals(ERole.ROLE_MENTEE)) {
            Mentee mentee = menteeRepository.findByUser(user);
            return new ResponseEntity<>(mentee, HttpStatus.OK);
        }
        return new ResponseEntity<>("Some ERROR!!!!", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> editUserProfile(String email, UpdateMentorRequest signupMentorRequest) {
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
}
