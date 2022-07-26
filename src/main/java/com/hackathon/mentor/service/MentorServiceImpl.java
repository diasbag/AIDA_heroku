package com.hackathon.mentor.service;

import com.hackathon.mentor.exceptions.AccountNotFound;
import com.hackathon.mentor.models.*;
import com.hackathon.mentor.payload.request.UpdateMentorRequest;
import com.hackathon.mentor.payload.response.MentorProfileResponse;
import com.hackathon.mentor.payload.response.MentorsResponse;
import com.hackathon.mentor.repository.*;
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

    private final MenteeRepository menteeRepository;

    private final UserRepository userRepository;

    private final SubscribeRepository subscribeRepository;

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

    @Override
    public ResponseEntity<?> getMentorById(Long id) {
        Mentor mentor = mentorRepository.findById(id).orElse(null);
        if (mentor == null) {
            return new ResponseEntity<>("Not Found!!!", HttpStatus.NOT_FOUND);
        }
        MentorProfileResponse mentorProfileResponse = new MentorProfileResponse();
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
        mentorProfileResponse.setWork(mentor.getWork());
        mentorProfileResponse.setUserInfo(mentor.getUserInfo());
        mentorProfileResponse.setUniversity(mentor.getUniversity());

        return new ResponseEntity<>(mentorProfileResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getProfile(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        ERole role = user.getRoles().get(0).getName();
        if (role.name().equals("ROLE_MENTOR") ) {
            Mentor mentor = mentorRepository.findByUser(user);
            return new ResponseEntity<>(mentor, HttpStatus.OK);
        }
        if (role.equals(ERole.ROLE_MENTEE)) {
            Mentee mentee = menteeRepository.findByUser(user);
            return new ResponseEntity<>(mentee, HttpStatus.OK);
        }
        return null;
    }

    @Override
    public ResponseEntity<?> updateMentor(String email, UpdateMentorRequest signupMentorRequest) {
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

    @Override
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

    @Override
    public ResponseEntity<?> confirm(Long id, String email) {
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

    @Override
    public ResponseEntity<?> reject(Long id, String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        Mentor mentor = mentorRepository.findByUser(user);
        Mentee mentee = menteeRepository.findById(id).orElse(null);
        Subscribe subscribe = subscribeRepository.getByMentorAndMentee(mentor, mentee);
        Long sid = subscribe.getId();
        System.out.println("qwerqwerqwerqwerqwer   " + subscribe.getId());
        subscribeRepository.deleteById(sid);

        return new ResponseEntity<>("Success" , HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getMentorMentees(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        Mentor mentor = mentorRepository.findByUser(user);
        return new ResponseEntity<>(mentor.getMentees(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> deleteFollower(Long id, String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        Mentor mentor = mentorRepository.findByUser(user);

        Mentee mentee = menteeRepository.findById(id).orElseThrow(() -> new RuntimeException("Mentee Not Found!!!!"));

        mentor.getMentees().remove(mentee);

        mentorRepository.save(mentor);
        subscribeRepository.deleteByMentorAndMentee(mentor, mentee);

        return new ResponseEntity<>(mentor, HttpStatus.OK);
    }
}
