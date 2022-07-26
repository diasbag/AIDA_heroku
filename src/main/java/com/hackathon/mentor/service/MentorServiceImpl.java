package com.hackathon.mentor.service;

import com.hackathon.mentor.exceptions.AccountNotFound;
import com.hackathon.mentor.models.*;
import com.hackathon.mentor.payload.response.MentorsResponse;
import com.hackathon.mentor.repository.MenteeRepository;
import com.hackathon.mentor.repository.MentorRepository;
import com.hackathon.mentor.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MentorServiceImpl implements MentorService {

    private final MentorRepository mentorRepository;

    private final RatingRepository ratingRepository;

    private final MenteeRepository menteeRepository;

    @Override
    public ResponseEntity<?> rateMentor(Long id, double rate) {
        Mentor mentor = mentorRepository.findById(id).orElseThrow(() -> new AccountNotFound("Mentor not found"));
        Rating rating = mentor.getRating();
        double res = (rating.getRating()+rate)/(rating.getPeopleCount()+1);
        rating.setRating(res);
        ratingRepository.save(rating);
        mentor.setRating(rating);
        mentorRepository.save(mentor);
        return new ResponseEntity<>(mentor, HttpStatus.OK);
    }
    @Override
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
    @Override
    public ResponseEntity<?> filtration(String country, String major, String university) {
        List<Mentor> mentors = null;
        if (country != null && major != null && university == null) {
            mentors = mentorRepository.findByCountryAndMajor(country , major);
        } else if (country != null && major == null && university == null) {
            mentors = mentorRepository.findByCountry(country);
        } else if (country == null && major != null && university == null) {
            mentors = mentorRepository.findByMajor(major);
        } else if (country != null && major == null) {
            mentors = mentorRepository.findByCountryAndUniversity(country, university);
        } else if (country == null && major != null) {
            mentors = mentorRepository.findByMajorAndUniversity(major, university);
        } else if (country != null) {
            mentors = mentorRepository.findByCountryAndUniversityAndMajor(country, university, major);
        } else if (university != null) {
            mentors = mentorRepository.findByUniversity(university);
        }
        List<MentorsResponse> mentorsResponseList = new ArrayList<>();
        if (mentors == null) {
            return new ResponseEntity<>("Not found", HttpStatus.NOT_FOUND);
        }
        for (Mentor mentor : mentors) {
            MentorsResponse mentorsResponse = new MentorsResponse();
            mentorsResponse.setUser(mentor.getUser());
            mentorsResponse.setAge(mentor.getAge());
            mentorsResponse.setCountry(mentor.getCountry());
            mentorsResponse.setRating(mentor.getRating());
            mentorsResponse.setIin(mentor.getIin());
            mentorsResponse.setMajor(mentor.getMajor());
            mentorsResponse.setNumber(mentor.getNumber());
            mentorsResponse.setSchool(mentor.getSchool());
            mentorsResponse.setUniversity(mentor.getUniversity());
            mentorsResponse.setUserInfo(mentor.getUserInfo());
            mentorsResponse.setWork(mentor.getWork());
            mentorsResponseList.add(mentorsResponse);
        }
        return new ResponseEntity<>(mentorsResponseList, HttpStatus.OK);
    }
}
