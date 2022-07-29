package com.hackathon.mentor.service.serviceImpl;

import com.hackathon.mentor.exceptions.AccountNotFound;
import com.hackathon.mentor.models.*;
import com.hackathon.mentor.payload.request.SignupUpdateMentorRequest;
import com.hackathon.mentor.payload.response.MentorProfileResponse;
import com.hackathon.mentor.payload.response.MentorsResponse;
import com.hackathon.mentor.repository.*;
import com.hackathon.mentor.service.MentorService;
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
        for (Mentor mentor : mentors) {
            MentorsResponse mentorsResponse = new MentorsResponse();
            mentorsResponse.setId(mentor.getId());
            mentorsResponse.setUser(mentor.getUser());
            mentorsResponse.setAge(mentor.getAge());
//            mentorsResponse.setNumber(mentor.getNumber());
//            mentorsResponse.setRating(mentor.getRating());
            mentorsResponse.setIin(mentor.getIin());
            mentorsResponse.setBachelorsMajor(mentor.getBachelorsMajor());
            mentorsResponse.setMastersMajor(mentor.getMastersMajor());
            mentorsResponse.setBachelorsUniversity(mentor.getBachelorsUniversity());
            mentorsResponse.setCountryOfBachelorsUniversity(mentor.getCountryOfBachelorsUniversity());
            mentorsResponse.setMastersUniversity(mentor.getMastersUniversity());
            mentorsResponse.setCountryOfMastersUniversity(mentor.getCountryOfMastersUniversity());
            mentorsResponse.setCountryOfResidence(mentor.getCountry());
            mentorsResponse.setWork(mentor.getWork());
            mentorsResponse.setUserInfo(mentor.getUserInfo());
            mentorsResponse.setSchool(mentor.getSchool());
            mentorsResponse.setYearOfGraduation(mentor.getYearOfGraduation());
            mentorsResponse.setSubjectOfInterest1(mentor.getSubjectOfInterest1());
            mentorsResponse.setSubjectOfInterest2(mentor.getSubjectOfInterest2());

            mentorsResponseList.add(mentorsResponse);
            mentorsResponse.setMenteesCount(mentor.getMentees().size());
        }
        return new ResponseEntity<>(mentorsResponseList, HttpStatus.OK);
    }

//    @Override
//    public ResponseEntity<?> filtration(String country, String major, String university) {
//        List<Mentor> mentors = null;
//        if (country != null && major != null && university == null) {
//            mentors = mentorRepository.findByCountryAndMajor(country , major);
//        } else if (country != null && major == null && university == null) {
//            mentors = mentorRepository.findByCountry(country);
//        } else if (country == null && major != null && university == null) {
//            mentors = mentorRepository.findByMajor(major);
//        } else if (country != null && major == null) {
//            mentors = mentorRepository.findByCountryAndUniversity(country, university);
//        } else if (country == null && major != null) {
//            mentors = mentorRepository.findByMajorAndUniversity(major, university);
//        } else if (country != null) {
//            mentors = mentorRepository.findByCountryAndUniversityAndMajor(country, university, major);
//        } else if (university != null) {
//            mentors = mentorRepository.findByUniversity(university);
//        }
//        List<MentorsResponse> mentorsResponseList = new ArrayList<>();
//        if (mentors == null) {
//            return new ResponseEntity<>("Not found", HttpStatus.NOT_FOUND);
//        }
//        for (Mentor mentor : mentors) {
//            MentorsResponse mentorsResponse = new MentorsResponse();
//            mentorsResponse.setId(mentor.getId());
//            mentorsResponse.setUser(mentor.getUser());
//            mentorsResponse.setAge(mentor.getAge());
//            mentorsResponse.setNumber(mentor.getNumber());
//            mentorsResponse.setRating(mentor.getRating());
//            mentorsResponse.setIin(mentor.getIin());
//            mentorsResponse.setBachelorsMajor(mentor.getBachelorsMajor());
//            mentorsResponse.setMastersMajor(mentor.getMastersMajor());
//            mentorsResponse.setBachelorsUniversity(mentorsResponse.getBachelorsUniversity());
//            mentorsResponse.setCountryOfBachelorsUniversity(mentor.getCountryOfBachelorsUniversity());
//            mentorsResponse.setMastersUniversity(mentor.getMastersUniversity());
//            mentorsResponse.setCountryOfMastersUniversity(mentor.getCountryOfMastersUniversity());
//            mentorsResponse.setCountry(mentor.getCountry());
//            mentorsResponse.setWork(mentor.getWork());
//            mentorsResponse.setUserInfo(mentor.getUserInfo());
//            mentorsResponse.setSchool(mentor.getSchool());
//            mentorsResponse.setYearOfGraduation(mentorsResponse.getYearOfGraduation());
//            mentorsResponse.setSubject1(mentorsResponse.getSubject1());
//            mentorsResponse.setSubject2(mentor.getSubject2());
//
//            mentorsResponseList.add(mentorsResponse);
//        }
//        return new ResponseEntity<>(mentorsResponseList, HttpStatus.OK);
//    }

    @Override
    public ResponseEntity<?> getMentorById(Long id) {
        Mentor mentor = mentorRepository.findById(id).orElse(null);
        if (mentor == null) {
            return new ResponseEntity<>("Not Found!!!", HttpStatus.NOT_FOUND);
        }
        log.info("get mentor...");
        MentorProfileResponse mentorProfileResponse = new MentorProfileResponse();
        mentorProfileResponse.setFirstname(mentor.getUser().getFirstname());
        mentorProfileResponse.setLastname(mentor.getUser().getLastname());
        mentorProfileResponse.setMiddlename(mentor.getUser().getMiddlename());
        mentorProfileResponse.setEmail(mentor.getUser().getEmail());
        mentorProfileResponse.setImage(mentor.getUser().getImage());
        mentorProfileResponse.setAge(mentor.getAge());
//        mentorProfileResponse.setNumber(mentor.getNumber());
//        mentorProfileResponse.setRating(mentor.getRating());
        mentorProfileResponse.setIin(mentor.getIin());
        mentorProfileResponse.setBachelorsMajor(mentor.getBachelorsMajor());
        mentorProfileResponse.setCountryOfBachelorsUniversity(mentor.getCountryOfBachelorsUniversity());
        mentorProfileResponse.setMastersUniversity(mentor.getMastersUniversity());
        mentorProfileResponse.setCountryOfMastersUniversity(mentor.getCountryOfMastersUniversity());
        mentorProfileResponse.setCountryOfResidence(mentor.getCountry());
        mentorProfileResponse.setMenteesCount(mentor.getMentees().size());
        mentorProfileResponse.setWork(mentor.getWork());
        mentorProfileResponse.setUserInfo(mentor.getUserInfo());
        mentorProfileResponse.setSchool(mentor.getSchool());
        mentorProfileResponse.setYearOfGraduation(mentor.getYearOfGraduation());
        mentorProfileResponse.setSubjectOfInterest1(mentor.getSubjectOfInterest1());
        mentorProfileResponse.setSubjectOfInterest2(mentor.getSubjectOfInterest2());
        mentorProfileResponse.setMenteesCount(mentor.getMentees().size());
        log.info("Get mentor by id" + mentorProfileResponse);
        return new ResponseEntity<>(mentorProfileResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getProfile(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new AccountNotFound("user with email " + email));
        ERole role = user.getRoles().get(0).getName();
        log.info("get active user profile ...");
        if (role.name().equals("ROLE_MENTOR") ) {
            Mentor mentor = mentorRepository.findByUser(user);
            return new ResponseEntity<>(mentor, HttpStatus.OK);
        }
        if (role.equals(ERole.ROLE_MENTEE)) {
            Mentee mentee = menteeRepository.findByUser(user);
            return new ResponseEntity<>(mentee, HttpStatus.OK);
        }
        log.info("Active user profile" + user);
        return null;
    }

    @Override
    public ResponseEntity<?> updateMentor(String email, SignupUpdateMentorRequest signupMentorRequest) {
        User user = userRepository.getByEmail(email);
        Mentor mentor = mentorRepository.findByUser(user);
        log.info("updating mentor profile....");
        user.setEmail(signupMentorRequest.getEmail());
        user.setFirstname(signupMentorRequest.getFirstname());
        user.setLastname(signupMentorRequest.getLastname());
        user.setMiddlename(signupMentorRequest.getMiddlename());
        userRepository.save(user);

        mentor.setAge(signupMentorRequest.getAge());
        mentor.setIin(signupMentorRequest.getIin());
        mentor.setBachelorsUniversity(signupMentorRequest.getBachelorsUniversity());
        mentor.setCountry(signupMentorRequest.getCountryOfResidence());
//        mentor.setNumber(signupMentorRequest.getNumber());
        mentor.setWork(signupMentorRequest.getWork());
        mentor.setUserInfo(signupMentorRequest.getUserInfo());
        mentor.setSchool(signupMentorRequest.getSchool());
        mentor.setSubjectOfInterest1(signupMentorRequest.getSubjectOfInterest1());
        mentor.setSubjectOfInterest2(signupMentorRequest.getSubjectOfInterest2());
        mentor.setBachelorsUniversity(signupMentorRequest.getBachelorsUniversity());
        mentor.setMastersUniversity(signupMentorRequest.getCountryOfMastersUniversity());
        mentor.setYearOfGraduation(signupMentorRequest.getYearOfGraduation());
        mentor.setCountryOfBachelorsUniversity(signupMentorRequest.getCountryOfBachelorsUniversity());
        mentor.setCountryOfMastersUniversity(signupMentorRequest.getCountryOfMastersUniversity());
        mentor.setBachelorsMajor(signupMentorRequest.getBachelorsMajor());
        mentor.setMastersMajor(signupMentorRequest.getMastersMajor());

        mentorRepository.save(mentor);
        log.info("mentor successfully updated!!!");
        return ResponseEntity.ok("User updated successfully!");
    }

    @Override
    public ResponseEntity<?> getMySubscribers(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        Mentor mentor = mentorRepository.findByUser(user);
        log.info("get mentor wait list...");
        List<Subscribe> subscribes = subscribeRepository.findByMentor(mentor);

        List<Mentee> mentees = new ArrayList<>();
        for (Subscribe subscribe : subscribes) {
            mentees.add(subscribe.getMentee());
        }
        return new ResponseEntity<>(mentees, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> confirm(Long id, String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        Mentor mentor = mentorRepository.findByUser(user);

        if (mentor.getMentees().size() == 3) {
            return new ResponseEntity<>("You have 3 mentees" , HttpStatus.I_AM_A_TEAPOT);
        }

        Mentee mentee = menteeRepository.findById(id).orElse(null);

        Subscribe subscribe = subscribeRepository.getByMentorAndMentee(mentor, mentee);
        mentor.getMentees().add(mentee);
        mentorRepository.save(mentor);
        Long sid = subscribe.getId();
        subscribeRepository.deleteById(sid);
        log.info("Mentor confirmed mentee!!!");
        return new ResponseEntity<>("Success!!!", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> reject(Long id, String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        Mentor mentor = mentorRepository.findByUser(user);
        Mentee mentee = menteeRepository.findById(id).orElse(null);
        Subscribe subscribe = subscribeRepository.getByMentorAndMentee(mentor, mentee);
        Long sid = subscribe.getId();
        subscribeRepository.deleteById(sid);
        log.info("Mentor rejected mentee!!!");
        return new ResponseEntity<>("Success" , HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getMentorMentees(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        Mentor mentor = mentorRepository.findByUser(user);
        log.info("Mentor menteeList!!!");
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
        log.info("Mentee has been removed!!!");
        return new ResponseEntity<>(mentor, HttpStatus.OK);
    }
}
