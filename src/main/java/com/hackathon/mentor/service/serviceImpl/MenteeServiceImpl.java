package com.hackathon.mentor.service.serviceImpl;

import com.hackathon.mentor.models.Mentee;
import com.hackathon.mentor.models.Mentor;
import com.hackathon.mentor.models.Subscribe;
import com.hackathon.mentor.models.User;
import com.hackathon.mentor.payload.request.SignupUpdateMenteeRequest;
import com.hackathon.mentor.payload.request.RatingRequest;
import com.hackathon.mentor.repository.*;
import com.hackathon.mentor.service.MenteeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenteeServiceImpl implements MenteeService {

    private final MentorRepository mentorRepository;

    private final MenteeRepository menteeRepository;

    private final UserRepository userRepository;

    private final RatingRepository ratingRepository;

    private final SubscribeRepository subscribeRepository;
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

    @Override
    public ResponseEntity<?> deleteMentor(Long id, String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        Mentee mentee = menteeRepository.findByUser(user);
        Mentor mentor = mentorRepository.findById(id).orElseThrow(() -> new RuntimeException("Mentee Not Found!!!!"));
        mentor.getMentees().remove(mentee);
        mentee.setMentor(null);
        menteeRepository.save(mentee);
        mentorRepository.save(mentor);
        subscribeRepository.deleteByMentorAndMentee(mentor, mentee);
        log.info("Mentee has been removed!!!");
        return new ResponseEntity<>(mentor, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getWaitList(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        Mentee mentee = menteeRepository.findByUser(user);
        log.info("get mentee wait list...");
        List<Subscribe> subscribes = subscribeRepository.findByMentee(mentee);
        List<Mentor> mentors = new ArrayList<>();
        for (Subscribe subscribe : subscribes) {
            mentors.add(subscribe.getMentor());
        }
        return new ResponseEntity<>(mentors, HttpStatus.OK);
    }


}
