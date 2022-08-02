package com.hackathon.mentor.service.serviceImpl;

import com.hackathon.mentor.exceptions.AccountConflict;
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

import javax.transaction.Transactional;
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
    private final RatingNotificationRepository ratingNotificationRepository;

    @Override
    public ResponseEntity<?> getUserProfile(User user) {
        ERole role = user.getRoles().get(0).getName();
        if (role.name().equals("ROLE_MENTOR") ) {
            Mentor mentor = mentorRepository.findByUser(user).orElseThrow(() ->
                    new AccountNotFound("mentor - " + user));
            return new ResponseEntity<>(mentor, HttpStatus.OK);
        }
        if (role.name().equals("ROLE_MENTEE")) {
            Mentee mentee = menteeRepository.findByUser(user).orElseThrow(() ->
                    new AccountNotFound("user - " + user));
            return new ResponseEntity<>(mentee, HttpStatus.OK);
        }
        return new ResponseEntity<>("Some ERROR!!!!", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<?> getMentors() {
        log.info("getting all mentors ...");
        List<Mentor> mentors = mentorRepository.getAll();
        List<MentorsResponse> mentorsResponseList = new ArrayList<>();
        for (Mentor mentor : mentors) {
            MentorsResponse mentorsResponse = new MentorsResponse();
            mentorsResponse.setId(mentor.getId());
            mentorsResponse.setUser(mentor.getUser());
            mentorsResponse.setAge(mentor.getAge());
            mentorsResponse.setFirstname(mentor.getUser().getFirstname());
            mentorsResponse.setMiddlename(mentor.getUser().getMiddlename());
            mentorsResponse.setLastname(mentor.getUser().getLastname());
//            mentorsResponse.setNumber(mentor.getNumber());
//            mentorsResponse.setRating(mentor.getRating());
            mentorsResponse.setIin(mentor.getIin());
            mentorsResponse.setImage(mentor.getUser().getImage());
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
            mentorsResponse.setListOfSkills(mentor.getListOfSkills());
            mentorsResponse.setRating(mentor.getUser().getRating());
            mentorsResponseList.add(mentorsResponse);
            mentorsResponse.setMenteesCount(mentor.getMentees().size());
        }
        log.info("got all mentors " + mentorsResponseList + " <<<");
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
        log.info("get mentor...");
        Mentor mentor = mentorRepository.findById(id).orElseThrow(() -> new AccountNotFound("mentor with id " + id));
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
        mentorProfileResponse.setRating(mentor.getUser().getRating());
        mentorProfileResponse.setListOfSkills(mentor.getListOfSkills());
        log.info("Get mentor by id" + mentorProfileResponse + " <<<");
        return new ResponseEntity<>(mentorProfileResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getProfile(String email) {
        log.info("get active user profile ...");
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new AccountNotFound("user with email " + email));
        ERole role = user.getRoles().get(0).getName();
        if (role.name().equals("ROLE_MENTOR") ) {
            Mentor mentor = mentorRepository.findByUser(user).orElseThrow(() ->
                    new AccountNotFound("user with email " + email));
            return new ResponseEntity<>(mentor, HttpStatus.OK);
        }
        if (role.equals(ERole.ROLE_MENTEE)) {
            Mentee mentee = menteeRepository.findByUser(user).orElseThrow(() ->
                    new AccountNotFound("user - " + user));
            return new ResponseEntity<>(mentee, HttpStatus.OK);
        }
        log.info("Active user profile" + user + " <<<");
        return null;
    }

    @Override
    public ResponseEntity<?> updateMentor(String email, SignupUpdateMentorRequest signupMentorRequest) {
        log.info("updating mentor profile....");
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AccountNotFound("user - " + email));
        Mentor mentor = mentorRepository.findByUser(user).orElseThrow(() ->
                new AccountNotFound("user with email " + email));

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
        mentor.setListOfSkills(signupMentorRequest.getListOfSkills());
        mentorRepository.save(mentor);
        log.info("mentor's profile is successfully updated " + mentor + " <<<");
        return ResponseEntity.ok("User updated successfully!");
    }

    @Override
    public ResponseEntity<?> getMySubscribers(String email) {
        log.info("getting mentor subscribers ...");
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AccountNotFound("user - " + email));
        Mentor mentor = mentorRepository.findByUser(user).orElseThrow(() ->
                new AccountNotFound("user with email " + email));

        List<Subscribe> subscribes = subscribeRepository.findByMentor(mentor);
        List<Mentee> mentees = new ArrayList<>();
        for (Subscribe subscribe : subscribes) {
            mentees.add(subscribe.getMentee());
        }
        log.info("mentor waiting list: " + mentees + " <<<");
        return new ResponseEntity<>(mentees, HttpStatus.OK);
    }
    @Transactional
    @Override
    public ResponseEntity<?> confirm(Long id, String email) {
        log.info("mentor confirmation started ...");
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AccountNotFound("user with id " + id));
        Mentor mentor = mentorRepository.findByUser(user).orElseThrow(() ->
                new AccountNotFound("user with email " + email));
        if (mentor.getMentees().size() == 3) {
            return new ResponseEntity<>("You have 3 mentees" , HttpStatus.I_AM_A_TEAPOT);
        }
        Mentee mentee = menteeRepository.findById(id).orElseThrow(() -> new AccountNotFound("mentee with id " + id));
        if (mentee.getMentor() != null) {throw new AccountConflict("mentee already have mentor " + mentor);
        }
        Subscribe subscribe = subscribeRepository.getByMentorAndMentee(mentor, mentee).orElseThrow(() ->
                new AccountNotFound(" subscribe: " + mentor + " and " + mentee));
        mentor.getMentees().add(mentee);
        mentee.setMentor(mentor);
        RatingNotification ratingNotification = new RatingNotification(mentor, mentee);
        ratingNotificationRepository.save(ratingNotification);
        menteeRepository.save(mentee);
        mentorRepository.save(mentor);
        Long sid = subscribe.getId();
        subscribeRepository.deleteById(sid);
        log.info("Mentor confirmed mentee " + mentor + " + " + " <<<");
        return new ResponseEntity<>("Success!!!", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> reject(Long id, String email) {
        log.info("mentor reject started ...");
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AccountNotFound("user - " + email));
        Mentor mentor = mentorRepository.findByUser(user).orElseThrow(() ->
                new AccountNotFound("user with email " + email));
        Mentee mentee = menteeRepository.findById(id).orElse(null);
        Subscribe subscribe = subscribeRepository.getByMentorAndMentee(mentor, mentee).orElseThrow(() ->
                new AccountNotFound(" subscribe: " + mentor + " and " + mentee));
        Long sid = subscribe.getId();
        subscribeRepository.deleteById(sid);
        log.info("Mentor rejected mentee " + mentor + " + " + mentee + " <<<");
        return new ResponseEntity<>("Success" , HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getMentorMentees(String email) {
        log.info("getting mentor's mentees ...");
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AccountNotFound("user - " + email));
        Mentor mentor = mentorRepository.findByUser(user).orElseThrow(() ->
                new AccountNotFound("user with email " + email));
        log.info("Mentor menteeList " + mentor.getMentees() + " <<<");
        return new ResponseEntity<>(mentor.getMentees(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> deleteFollower(Long id, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AccountNotFound("user - " + email));
        Mentor mentor = mentorRepository.findByUser(user).orElseThrow(() ->
                new AccountNotFound("user with email " + email));
        Mentee mentee = menteeRepository.findById(id).orElseThrow(() ->
                new AccountNotFound("mentee with id - " + id));
        mentor.getMentees().remove(mentee);
        mentee.setMentor(null);
        menteeRepository.save(mentee);
        mentorRepository.save(mentor);
        if(subscribeRepository.findByMentorAndMentee(mentor, mentee).isPresent()) {
            subscribeRepository.deleteByMentorAndMentee(mentor, mentee);
        } else {
            throw new AccountConflict("there is no connection between - " + mentor + " and " + mentee);
        }
        log.info("Mentee has been removed!!!");
        return new ResponseEntity<>(mentor, HttpStatus.OK);
    }
}
