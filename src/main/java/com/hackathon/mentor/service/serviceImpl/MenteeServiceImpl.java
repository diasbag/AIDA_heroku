package com.hackathon.mentor.service.serviceImpl;

import com.hackathon.mentor.exceptions.AccountNotFound;
import com.hackathon.mentor.models.*;
import com.hackathon.mentor.payload.request.SignupUpdateMenteeRequest;
import com.hackathon.mentor.payload.response.MentorsResponse;
import com.hackathon.mentor.repository.*;
import com.hackathon.mentor.service.MenteeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenteeServiceImpl implements MenteeService {

    private final MentorRepository mentorRepository;

    private final MenteeRepository menteeRepository;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    private final SubscribeRepository subscribeRepository;

//    private final MentorHistoryRepository mentorHistoryRepository;
    private final RatingNotificationRepository ratingNotificationRepository;

    private final FileRepository fileRepository;

    @Override
    public ResponseEntity<?> getAllMentees() {
        List<Mentee> mentees = menteeRepository.getAll();
        return new ResponseEntity<>(mentees, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getMenteeById(Long id) {
        Mentee mentee = menteeRepository.findById(id).orElseThrow(() -> new AccountNotFound("mentee - " + id));
        if (mentee == null) {
            return new ResponseEntity<>("Not Found!!!", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(mentee, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> editProfile(String email, SignupUpdateMenteeRequest updateMenteeRequest) {
        log.info("Loading mentee profile...");
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AccountNotFound("user - " + email));
        Mentee mentee = menteeRepository.findByUser(user).orElseThrow(() -> new AccountNotFound("user - " + user));
        user.setFirstname(updateMenteeRequest.getFirstname());
        user.setLastname(updateMenteeRequest.getLastname());
        user.setEmail(updateMenteeRequest.getEmail());
        user.setTelegram(updateMenteeRequest.getTelegram());
        userRepository.save(user);
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        modelMapper.map(updateMenteeRequest, mentee);
        menteeRepository.save(mentee);
        log.info("Mentee profile Updated!!!");
        return new ResponseEntity<>(mentee, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> deleteMentor(Long id, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AccountNotFound("user - " + email));
        Mentee mentee = menteeRepository.findByUser(user).orElseThrow(() ->
                new AccountNotFound("mentee - " + user));
        Mentor mentor = mentorRepository.findById(id).orElseThrow(() -> new AccountNotFound("mentor - " + id));
        mentor.getMentees().remove(mentee);
        mentee.setMentor(null);
        List<RatingNotification> ratingNotificationList =
                ratingNotificationRepository.findRatingNotificationByMentorAndMentee(mentor,mentee).orElseThrow(
                        () -> new AccountNotFound("rating notification mentor - " + mentor +
                                " and mentee - " + mentee));
        RatingNotification ratingNotification = ratingNotificationList.get(ratingNotificationList.size() - 1);
        ratingNotification.setDateOfEnd(Date.from(Instant.now()));
//        MentorHistory mentorHistory = mentorHistoryRepository.findByMentorAndMentee(mentor, mentee);
//        mentorHistory.setEndDate(Date.from(Instant.now()));
//        mentorHistoryRepository.save(mentorHistory);
        ratingNotificationRepository.save(ratingNotification);
        menteeRepository.save(mentee);
        mentorRepository.save(mentor);
        subscribeRepository.deleteByMentorAndMentee(mentor, mentee);
        fileRepository.deleteAllByMentorAndMentee(mentor, mentee);
        log.info("Mentor has been removed " + mentor + " <<<");
        return new ResponseEntity<>(mentor, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getWaitList(String email) {
        log.info("get mentee wait list...");
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new AccountNotFound("user with email " + email));
        Mentee mentee = menteeRepository.findByUser(user).orElseThrow(() -> new AccountNotFound("user - " + user));

        List<Subscribe> subscribes = subscribeRepository.findByMentee(mentee);
        List<Mentor> mentors = new ArrayList<>();
        for (Subscribe subscribe : subscribes) {
            mentors.add(subscribe.getMentor());
        }
        log.info("waiting list was retrieved <<<");
        return new ResponseEntity<>(mentors, HttpStatus.OK);
    }

    @Override
    public Boolean isSubscribe(String email, Long id) {
        log.info("get mentee wait list...");
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new AccountNotFound("mentor with email " + email));
        Mentee mentee = menteeRepository.findByUser(user).orElseThrow(() -> new AccountNotFound("user - " + user));

        List<Subscribe> subscribes = subscribeRepository.findByMentee(mentee);
        Mentor mentor = mentorRepository.findById(id).orElseThrow(() ->
                new AccountNotFound("mentor with id " + id));
        for (Subscribe subscribe : subscribes) {
            if (subscribe.getMentor().equals(mentor)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public MentorsResponse getMyMentor() {
        log.info("finding mentor of mentee ...");
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AccountNotFound("user - " + email));
        Mentee mentee = menteeRepository.findByUser(user).orElseThrow(() ->
                new AccountNotFound("mentee - " + user));
        Mentor mentor = mentee.getMentor();
        MentorsResponse mentorsResponse;
        if (mentor != null) {
            mentorsResponse = modelMapper.map(mentor.getUser(), MentorsResponse.class);
            modelMapper.map(mentor, mentorsResponse);
            mentorsResponse.setMenteesCount(mentor.getMentees().size());
        } else {
            mentorsResponse = null;
        }
        log.info("mentor was found " + mentor + " <<<");
        return mentorsResponse;
    }

    @Override
    public ResponseEntity<?> unSubscribe(Long id, String email) {
        log.info("mentee unsubscribe started ...");
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AccountNotFound("user - " + email));
//        Mentor mentor = mentorRepository.findByUser(user).orElseThrow(() ->
//                new AccountNotFound("user with email " + email));
        Mentee mentee = menteeRepository.findByUser(user).orElseThrow(() ->
                new AccountNotFound("user with email " + email));
        Mentor mentor = mentorRepository.findById(id).orElse(null);
//        Mentee mentee = menteeRepository.findById(id).orElse(null);
        Subscribe subscribe = subscribeRepository.getByMentorAndMentee(mentor, mentee).orElseThrow(() ->
                new AccountNotFound(" subscribe: " + mentor + " and " + mentee));
        Long sid = subscribe.getId();
        subscribeRepository.deleteById(sid);
        log.info("Mentee unsubscribed " + mentor + " + " + mentee + " <<<");
        return new ResponseEntity<>("Success" , HttpStatus.OK);
    }

    @Override
    public Boolean isMyMentor(String email, Long id) {
        log.info("get mentee's mentor status ...");
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new AccountNotFound("user with email " + email));
        Mentee mentee = menteeRepository.findByUser(user).orElseThrow(() ->
                new AccountNotFound("mentee - " + user));
        Mentor mentor = mentorRepository.findById(id).orElseThrow(() ->
                new AccountNotFound("mentor with id " + id));
        if (mentee.getMentor() == null)  { return false;}
        log.info("got mentee's mentor status <<<");
        return mentee.getMentor().equals(mentor);
    }
}
