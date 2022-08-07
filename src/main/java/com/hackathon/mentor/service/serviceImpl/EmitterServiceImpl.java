package com.hackathon.mentor.service.serviceImpl;

import com.hackathon.mentor.exceptions.AccountNotFound;
import com.hackathon.mentor.exceptions.EmitterGone;
import com.hackathon.mentor.models.*;
import com.hackathon.mentor.repository.*;
import com.hackathon.mentor.service.EmitterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmitterServiceImpl implements EmitterService {
    private final SSEEmitterRepository sseEmitterRepository;
    private final UserRepository userRepository;
    private final MentorRepository mentorRepository;
    private final MenteeRepository menteeRepository;
    private final PostRepository postRepository;
    public SerializableSSE addEmitter() {
        log.info("subscribing to notifications ...");
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        User user= userRepository.findByEmail(email).orElseThrow(() -> new AccountNotFound("user - " + email));
        SerializableSSE sseEmitter = new SerializableSSE(24 * 60 * 60 * 1000L);
        SSEEmitter forRepo = sseEmitterRepository.findByUser(user).orElse(new SSEEmitter(sseEmitter));
        sseEmitter.onCompletion(() -> sseEmitterRepository.deleteBySseEmitter(sseEmitter));
        sseEmitter.onTimeout(() -> sseEmitterRepository.deleteBySseEmitter(sseEmitter));
        forRepo.setSseEmitter(sseEmitter);
        sseEmitterRepository.save(forRepo);
        log.info("subscribed <<<");
        return sseEmitter;

    }
    @Override
    public void sendToRateNotification(Long raterID, Long toRateID, boolean raterIsMentor) {
        log.info("pushing {} notification for user {}", "notification for rating", raterID);
        Mentor mentor = null;
        Mentee mentee = null;
        if( raterIsMentor) {
            mentor = mentorRepository.findById(toRateID).orElseThrow(() -> new AccountNotFound(
                    "mentor with id - " + raterID));
        } else {
            mentee = menteeRepository.findById(toRateID).orElseThrow(() -> new AccountNotFound(
                    "mentee with id - " + raterID));
        }
        User user;
        if(raterIsMentor) {
            user = mentor.getUser();
        } else {
            user = mentee.getUser();
        }
        SSEEmitter sseEmitter = sseEmitterRepository.findByUser(user).orElseThrow(() ->
                new AccountNotFound("emitter us user - " + user.getEmail()));
        try {
                sseEmitter.getSseEmitter().send(SseEmitter
                        .event()
                        .name("rating")
                        .data("rate mentor/mentee  with id: " + raterID));

        } catch (IOException e) {
                sseEmitterRepository.delete(sseEmitter);
            if(raterIsMentor) {
                throw new EmitterGone("emitter of mentor - " + mentor.getUser().getEmail());
            } else {
                throw new EmitterGone("emitter of mentee - " + mentee.getUser().getEmail());
            }
        }
        log.info("to rate notification was sent <<<");
    }


    @Override
    public void sendNews(Long newsID) {
        log.info("sending news started ...");
        Post post = postRepository.findById(newsID).orElseThrow(() -> new AccountNotFound("post - " + newsID));
        List<SSEEmitter> allEmitters = sseEmitterRepository.findAll();
        for (SSEEmitter sseEmitter: allEmitters) {
            try {
                sseEmitter.getSseEmitter().send(SseEmitter
                        .event()
                        .name("news")
                        .data("check news with id: " + post.getId() + ". Post - " + post));

            } catch (IOException e) {
                sseEmitterRepository.delete(sseEmitter);
            }
        }
        log.info("news were sent <<<");
    }

    @Override
    public void sendSubscriptionNotification(Long mentorID, Long menteeID) {
        log.info("pushing {} notification for user {}", "\"subscription\"", mentorID);
        Mentor mentor = mentorRepository.findById(mentorID).orElseThrow(() -> new AccountNotFound(
                "mentor with id - " + mentorID));
        User user = mentor.getUser();
        SSEEmitter sseEmitter = sseEmitterRepository.findByUser(user).orElseThrow(() ->
                new AccountNotFound("emitter us user - " + user.getEmail()));
        try {
            sseEmitter.getSseEmitter().send(SseEmitter
                    .event()
                    .name("subscription")
                    .data("you have new subscription of mentee with id: " + menteeID));
        } catch (IOException e) {
            sseEmitterRepository.delete(sseEmitter);
            throw new EmitterGone("emitter of mentor - " + mentor.getUser().getEmail());
        }
        log.info("subscription notification was sent <<<");
    }

    @Override
    public void confirmationNotification(Long mentorID, Long menteeID) {
        log.info("pushing {} notification for user {}", "\"confirmation\"", mentorID);
        Mentee mentee = menteeRepository.findById(mentorID).orElseThrow(() -> new AccountNotFound(
                "mentor with id - " + menteeID));
        User user = mentee.getUser();
        SSEEmitter sseEmitter = sseEmitterRepository.findByUser(user).orElseThrow(() ->
                new AccountNotFound("emitter us user - " + user.getEmail()));
        try {
            sseEmitter.getSseEmitter().send(SseEmitter
                    .event()
                    .name("confirmation")
                    .data("you have new confirmation from mentor with id: " + mentorID));
        } catch (IOException e) {
            sseEmitterRepository.delete(sseEmitter);
            throw new EmitterGone("emitter of mentor - " + mentee.getUser().getEmail());
        }
        log.info("confirmation notification was sent <<<");
    }
}
