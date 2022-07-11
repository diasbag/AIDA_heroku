package com.hackathon.mentor.security.services;

import com.hackathon.mentor.models.Mentor;
import com.hackathon.mentor.models.User;
import com.hackathon.mentor.payload.request.UpdateMentorRequest;
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

    public ResponseEntity<?> rateMentor(Long id, double rate) {
        Mentor mentor = mentorRepository.findById(id).orElse(null);
        long count  = mentor.getPeopleCount() +1;
        double res = (rate + mentor.getRating())/count;
        mentor.setRating(res);
        mentor.setPeopleCount(count);
        mentorRepository.save(mentor);
        return new ResponseEntity<>(mentor, HttpStatus.OK);
    }
}
