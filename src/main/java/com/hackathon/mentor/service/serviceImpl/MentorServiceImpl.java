package com.hackathon.mentor.service.serviceImpl;

import com.hackathon.mentor.exceptions.AccountConflict;
import com.hackathon.mentor.exceptions.AccountNotFound;
import com.hackathon.mentor.models.*;
import com.hackathon.mentor.payload.request.SignupUpdateMentorRequest;
import com.hackathon.mentor.payload.response.MentorProfileResponse;
import com.hackathon.mentor.payload.response.MentorsResponse;
import com.hackathon.mentor.repository.*;
import com.hackathon.mentor.service.MentorService;
import com.hackathon.mentor.utils.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class MentorServiceImpl implements MentorService {

    private final MentorRepository mentorRepository;

    private final MenteeRepository menteeRepository;

    private final UserRepository userRepository;

    private final SubscribeRepository subscribeRepository;
    private final RatingNotificationRepository ratingNotificationRepository;

//    private final MentorHistoryRepository mentorHistoryRepository;

    private final MailService mailService;
    private final ModelMapper modelMapper = new ModelMapper();

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
    public ResponseEntity<?> getMentors(Integer page) {
        log.info("getting all mentors ...");
        Pageable paging =  PageRequest.of(page, 10);
        Page<Mentor>  pageMentors = mentorRepository.findAll(paging);
        List<MentorsResponse> mentorsResponseList = new ArrayList<>();
        Map<String, Object> result = new HashMap<>();
        for (Mentor mentor : pageMentors) {
            MentorsResponse mentorsResponse = modelMapper.map(mentor.getUser(), MentorsResponse.class);
            modelMapper.map(mentor, mentorsResponse);
            mentorsResponse.setPassword(null);
            mentorsResponse.setMenteesCount(mentor.getMentees().size());
            mentorsResponseList.add(mentorsResponse);
            result.put("mentors", mentorsResponseList);
            result.put("totalPages", pageMentors.getTotalPages());
        }
        log.info("got all mentors " + mentorsResponseList + " <<<");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getMentorById(Long id) {
        log.info("get mentor...");
        Mentor mentor = mentorRepository.findById(id).orElseThrow(() -> new AccountNotFound("mentor with id " + id));
        MentorProfileResponse mentorProfileResponse = modelMapper.map(mentor.getUser(), MentorProfileResponse.class);
        modelMapper.map(mentor, mentorProfileResponse);
        mentorProfileResponse.setMenteesCount(mentor.getMentees().size());
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
        modelMapper.map(signupMentorRequest, mentor);
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
//        MentorHistory mentorHistory = new MentorHistory();
        ratingNotification.setDateOfStart(Date.from(Instant.now()));
//        mentorHistory.setMentor(mentor);
//        mentorHistory.setMentee(mentee);
//        mentorHistory.setStartDate(Date.from(Instant.now()));
//        mentorHistoryRepository.save(mentorHistory);
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
        List<RatingNotification> ratingNotificationList = ratingNotificationRepository
                .findRatingNotificationByMentorAndMentee(mentor,mentee).orElseThrow(() -> new AccountNotFound(
                        "rating notification mentor - " + mentor + " and mentee - " + mentee));
        RatingNotification ratingNotification = ratingNotificationList.get(ratingNotificationList.size() - 1);
        ratingNotification.setDateOfEnd(Date.from(Instant.now()));
//        MentorHistory mentorHistory = mentorHistoryRepository.findByMentorAndMentee(mentor, mentee);
//        mentorHistory.setEndDate(Date.from(Instant.now()));
//        mentorHistoryRepository.save(mentorHistory);
        ratingNotificationRepository.save(ratingNotification);
        mentor.getMentees().remove(mentee);
        mentee.setMentor(null);
        mailService.sendDeleteMailToMentee(mentee.getUser().getEmail(),
                mentor.getUser().getFirstname(),
                mentor.getUser().getLastname());
        menteeRepository.save(mentee);
        mentorRepository.save(mentor);
        log.info("Mentee has been removed!!!");
        return new ResponseEntity<>(mentor, HttpStatus.OK);
    }
}
