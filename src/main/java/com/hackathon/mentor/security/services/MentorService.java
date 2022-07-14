package com.hackathon.mentor.security.services;

import com.hackathon.mentor.models.*;
import com.hackathon.mentor.payload.response.MentorsResponse;
import com.hackathon.mentor.repository.MenteeRepository;
import com.hackathon.mentor.repository.MentorRepository;
import com.hackathon.mentor.repository.RatingRepository;
import com.hackathon.mentor.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service

public class MentorService {

    @Autowired
    private MentorRepository mentorRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired

    private MenteeRepository menteeRepository;

    public ResponseEntity<?> rateMentor(Long id, double rate) {
        Mentor mentor = mentorRepository.findById(id).orElse(null);
        Rating rating = mentor.getRating();
        double res = (rating.getRating()+rate)/(rating.getPeopleCount()+1);
        rating.setRating(res);
        ratingRepository.save(rating);
        mentor.setRating(rating);
        mentorRepository.save(mentor);
        return new ResponseEntity<>(mentor, HttpStatus.OK);
    }

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

    public ResponseEntity<?> filtration(String country, String major, String university) {
        List<Mentor> mentors = null;
        if (country != null && major != null && university == null) {
            mentors = mentorRepository.findByCountryAndMajor(country , major);
        } else if (country != null && major == null && university == null) {
            mentors = mentorRepository.findByCountry(country);
        } else if (country == null && major != null && university == null) {
            mentors = mentorRepository.findByMajor(major);
        } else if (country != null && major == null && university != null) {
            mentors = mentorRepository.findByCountryAndUniversity(country, university);
        } else if (country == null && major != null && university != null) {
            mentors = mentorRepository.findByMajorAndUniversity(major, university);
        } else if (country != null && major != null && university != null) {
            mentors = mentorRepository.findByCountryAndUniversityAndMajor(country, university, major);
        } else if (country == null && major == null && university != null) {
            mentors = mentorRepository.findByUniversity(university);
        }
        List<MentorsResponse> mentorsResponseList = new ArrayList<>();
        if (mentors == null) {
            return new ResponseEntity<>("Not found", HttpStatus.NOT_FOUND);
        }
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
        }
        return new ResponseEntity<>(mentorsResponseList, HttpStatus.OK);
    }
}
