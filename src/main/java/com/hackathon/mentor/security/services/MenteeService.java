package com.hackathon.mentor.security.services;

import com.hackathon.mentor.models.Mentor;
import com.hackathon.mentor.repository.MenteeRepository;
import com.hackathon.mentor.repository.MentorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class MenteeService {

    private MentorRepository mentorRepository;

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
}
