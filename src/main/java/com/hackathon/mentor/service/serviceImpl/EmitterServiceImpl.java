package com.hackathon.mentor.service.serviceImpl;

import com.hackathon.mentor.exceptions.AccountNotFound;
import com.hackathon.mentor.exceptions.EmitterGone;
import com.hackathon.mentor.models.*;
import com.hackathon.mentor.repository.MenteeRepository;
import com.hackathon.mentor.repository.MentorRepository;
import com.hackathon.mentor.repository.SSEEmitterRepository;
import com.hackathon.mentor.repository.UserRepository;
import com.hackathon.mentor.service.EmitterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmitterServiceImpl implements EmitterService {
    private final SSEEmitterRepository sseEmitterRepository;
    private final UserRepository userRepository;
    private final MentorRepository mentorRepository;
    private final MenteeRepository menteeRepository;
    public void addEmitter() {
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
    }
    @Override
    public void sendToRateNotification(Long raterID, Long toRateID) {
        log.info("pushing {} notification for user {}", "notification for rating", raterID);
        Mentor mentor = mentorRepository.findById(toRateID).orElse(null);
        Mentee mentee = menteeRepository.findById(toRateID).orElse(null);
        User user;
        if(mentor != null && mentee == null) {
            user = mentor.getUser();
        } else if (mentee != null && mentor == null) {
            user = mentee.getUser();
        } else {
            throw new AccountNotFound("mentor or mentee with id - " + toRateID);
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
            if(mentor != null) {
                throw new EmitterGone("emitter of mentor - " + mentor.getUser().getEmail());
            } else {
                throw new EmitterGone("emitter of mentee - " + mentee.getUser().getEmail());
            }
        }
    }

    @Override
    public SSEEmitter getEmitter(Long id) {
        return sseEmitterRepository.findById(id).orElse(null);
    }
}
