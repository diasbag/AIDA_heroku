package com.hackathon.mentor.service.serviceImpl;

import com.hackathon.mentor.exceptions.AccountNotFound;
import com.hackathon.mentor.models.Mentee;
import com.hackathon.mentor.models.Mentor;
import com.hackathon.mentor.models.Subscribe;
import com.hackathon.mentor.models.User;
import com.hackathon.mentor.repository.MenteeRepository;
import com.hackathon.mentor.repository.MentorRepository;
import com.hackathon.mentor.repository.SubscribeRepository;
import com.hackathon.mentor.repository.UserRepository;
import com.hackathon.mentor.service.EmitterService;
import com.hackathon.mentor.service.SubscribeService;
import com.hackathon.mentor.utils.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscribeServiceImpl implements SubscribeService {

    private final SubscribeRepository subscribeRepository;

    private final UserRepository userRepository;

    private final MenteeRepository menteeRepository;

    private final MentorRepository mentorRepository;

    private final MailService mailService;
    private final EmitterService emitterService;
    @Override
    public ResponseEntity<?> getSubscribers() {
        List<Subscribe> subscribeList = subscribeRepository.findAll();
        return new ResponseEntity<>(subscribeList , HttpStatus.OK);

    }


    @Override
    public ResponseEntity<?> subscribe(Long id, String email) {
        log.info("starting subscription ...");
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AccountNotFound("user - " + email));
        Mentee mentee = menteeRepository.findByUser(user).orElseThrow(() -> new AccountNotFound("user - " + user));
        Mentor mentor = mentorRepository.findById(id).orElse(null);
        if (subscribeRepository.findByMentorAndMentee(mentor, mentee).orElse(null) != null ||
                Objects.requireNonNull(mentor).getMentees().contains(mentee)) {
            return new ResponseEntity<>("You are already subscribed!!!!", HttpStatus.CONFLICT);
        }
        Subscribe subscribe = new Subscribe();
        Set<Mentee> mentees = new HashSet<>();
        mentees.add(mentee);
        subscribe.setMentee(mentees);
        subscribe.setMentor(mentor);
        menteeRepository.save(mentee);
        subscribeRepository.save(subscribe);
        mailService.sendSubscribeMail(mentor.getUser().getEmail(), mentee.getUser().getFirstname(),
                mentee.getUser().getLastname());
        mailService.sendSubscribeMailToMentee(mentee.getUser().getEmail(), mentor.getUser().getFirstname(),
                mentor.getUser().getLastname());
        emitterService.sendSubscriptionNotification(mentor.getId(), mentee.getId());
        log.info("Successfully subscribed " + mentor + " + " + mentee + " <<<");
        return new ResponseEntity<>("Success!!!", HttpStatus.OK);
    }
}
