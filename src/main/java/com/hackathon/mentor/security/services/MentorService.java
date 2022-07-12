package com.hackathon.mentor.security.services;

import com.hackathon.mentor.models.ERole;
import com.hackathon.mentor.models.Mentee;
import com.hackathon.mentor.models.Mentor;
import com.hackathon.mentor.models.User;
import com.hackathon.mentor.payload.request.UpdateMentorRequest;
import com.hackathon.mentor.payload.response.MentorProfileResponse;
import com.hackathon.mentor.repository.MenteeRepository;
import com.hackathon.mentor.repository.MentorRepository;
import com.hackathon.mentor.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class MentorService {

    private MentorRepository mentorRepository;
    private UserRepository userRepository;


    private MenteeRepository menteeRepository;

//    public ResponseEntity<?> rateMentor(Long id, double rate) {
//        Mentor mentor = mentorRepository.findById(id).orElse(null);
//        long count  = mentor.getPeopleCount() +1;
//        double res = (rate + mentor.getRating())/count;
//        mentor.setRating(res);
//        mentor.setPeopleCount(count);
//        mentorRepository.save(mentor);
//        return new ResponseEntity<>(mentor, HttpStatus.OK);
//    }

    public ResponseEntity<?> getUserProfile(User user) {
        ERole role = user.getRoles().get(0).getName();
        if (role.name().equals("ROLE_MENTOR") ) {
            Mentor mentor = mentorRepository.getByUser(user);
            return new ResponseEntity<>(mentor, HttpStatus.OK);
        }
        if (role.name().equals("ROLE_MENTEE")) {
            Mentee mentee = menteeRepository.findByUser(user);
            return new ResponseEntity<>(mentee, HttpStatus.OK);
        }
        return new ResponseEntity<>("Some ERROR!!!!", HttpStatus.BAD_REQUEST);
    }
}
