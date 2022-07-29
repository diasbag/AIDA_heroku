package com.hackathon.mentor.service.serviceImpl;

import com.hackathon.mentor.models.Mentee;
import com.hackathon.mentor.models.User;
import com.hackathon.mentor.payload.request.SignupUpdateMenteeRequest;
import com.hackathon.mentor.repository.MenteeRepository;
import com.hackathon.mentor.repository.MentorRepository;
import com.hackathon.mentor.repository.RatingRepository;
import com.hackathon.mentor.repository.UserRepository;
import com.hackathon.mentor.service.MenteeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenteeServiceImpl implements MenteeService {

    private final MentorRepository mentorRepository;

    private final MenteeRepository menteeRepository;

    private final UserRepository userRepository;

    private final RatingRepository ratingRepository;

    @Override
    public ResponseEntity<?> getAllMentees() {
        List<Mentee> mentees = menteeRepository.getAll();
        return new ResponseEntity<>(mentees, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getMenteeById(Long id) {
        Mentee mentee = menteeRepository.findById(id).orElse(null);
        if (mentee == null) {
            return new ResponseEntity<>("Not Found!!!", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(mentee, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> editProfile(String email, SignupUpdateMenteeRequest updateMenteeRequest) {
        User user = userRepository.getByEmail(email);
        Mentee mentee = menteeRepository.findByUser(user);
        log.info("Loading mentee profile...");
        user.setFirstname(updateMenteeRequest.getFirstname());
        user.setLastname(updateMenteeRequest.getLastname());
        user.setEmail(updateMenteeRequest.getEmail());

        userRepository.save(user);

        mentee.setSchool(updateMenteeRequest.getSchool());
        mentee.setIin(updateMenteeRequest.getIin());
//        mentee.setNumber(updateMenteeRequest.getNumber());
        mentee.setSubjectOfInterest1(updateMenteeRequest.getSubjectOfInterest1());
        mentee.setSubjectOfInterest2(updateMenteeRequest.getSubjectOfInterest2());
        menteeRepository.save(mentee);
        log.info("Mentee profile Updated!!!");
        return new ResponseEntity<>(mentee, HttpStatus.OK);
    }


}
