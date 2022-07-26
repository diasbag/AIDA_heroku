package com.hackathon.mentor.services;

import com.hackathon.mentor.models.*;
import com.hackathon.mentor.payload.response.MentorProfileResponse;
import com.hackathon.mentor.payload.response.MentorsResponse;
import com.hackathon.mentor.repository.*;
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

    @Autowired
    private SubscribeRepository subscribeRepository;

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


    public ResponseEntity<?> getMentorById(Long id) {
        Mentor mentor = mentorRepository.findById(id).orElse(null);
        if (mentor == null) {
            return new ResponseEntity<>("Not Found!!!", HttpStatus.NOT_FOUND);
        }
        MentorProfileResponse mentorProfileResponse = new MentorProfileResponse();
        //List<Post> posts = postRepository.getByMentor(mentor);
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
        //mentorProfileResponse.setPosts(posts);
        mentorProfileResponse.setWork(mentor.getWork());
        mentorProfileResponse.setUserInfo(mentor.getUserInfo());
        mentorProfileResponse.setUniversity(mentor.getUniversity());

        return new ResponseEntity<>(mentorProfileResponse, HttpStatus.OK);
    }

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

    public ResponseEntity<?> getMySubscribers(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        Mentor mentor = mentorRepository.findByUser(user);

        List<Subscribe> subscribes = subscribeRepository.findByMentor(mentor);

        List<Mentee> mentees = new ArrayList<>();
        for(int i = 0; i < subscribes.size(); i++) {
            mentees.add(subscribes.get(i).getMentee());
        }
        return new ResponseEntity<>(mentees, HttpStatus.OK);
    }

    public ResponseEntity<?> confirm(String email, Long id) {
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

    public ResponseEntity<?> reject(String email, Long id) {
        User user = userRepository.findByEmail(email).orElse(null);
        Mentor mentor = mentorRepository.findByUser(user);
        Mentee mentee = menteeRepository.findById(id).orElse(null);
        Subscribe subscribe = subscribeRepository.getByMentorAndMentee(mentor, mentee);
        Long sid = subscribe.getId();
        System.out.println("qwerqwerqwerqwerqwer   " + subscribe.getId());
        subscribeRepository.deleteById(sid);

        return new ResponseEntity<>("Success" , HttpStatus.OK);
    }

    public ResponseEntity<?> getMentorMentees(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        Mentor mentor = mentorRepository.findByUser(user);
        return new ResponseEntity<>(mentor.getMentees(), HttpStatus.OK);
    }

    public ResponseEntity<?> deleteFollower(String email, Long id) {
        User user = userRepository.findByEmail(email).orElse(null);
        Mentor mentor = mentorRepository.findByUser(user);

        Mentee mentee = menteeRepository.findById(id).orElseThrow(() -> new RuntimeException("Mentee Not Found!!!!"));

        mentor.getMentees().remove(mentee);

        mentorRepository.save(mentor);
        subscribeRepository.deleteByMentorAndMentee(mentor, mentee);

        return new ResponseEntity<>(mentor, HttpStatus.OK);
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
