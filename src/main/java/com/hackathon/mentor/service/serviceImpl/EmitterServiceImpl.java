package com.hackathon.mentor.service.serviceImpl;

import com.hackathon.mentor.exceptions.AccountNotFound;
import com.hackathon.mentor.exceptions.EmitterGone;
import com.hackathon.mentor.models.*;
import com.hackathon.mentor.repository.*;
import com.hackathon.mentor.service.EmitterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableAsync
public class EmitterServiceImpl implements EmitterService {
    private final SSEEmitterRepository sseEmitterRepository;
    private final UserRepository userRepository;
    private final MentorRepository mentorRepository;
    private final MenteeRepository menteeRepository;
    private final PostRepository postRepository;

    private final ExecutorService nonBlockingService = Executors
            .newCachedThreadPool();
    private List<SseEmitter> list = new ArrayList<>();
    @Async
    public SseEmitter addEmitter(String email) {
        log.info("subscribing to notifications ...");
        SseEmitter sseEmitter = new SseEmitter(-1L);
        list.add(sseEmitter);
//        User user= userRepository.findByEmail(email).orElseThrow(() -> new AccountNotFound("user - " + email));
//        SSEEmitter forRepo = sseEmitterRepository.findByUser(user).orElse(new SSEEmitter(
//                new SerializableSSE(24 * 60 * 60 * 1000L)));
//        SerializableSSE sseEmitter = forRepo.getSseEmitter();
//        forRepo.setUser(user);
//        sseEmitter.onTimeout(() -> sseEmitterRepository.deleteBySseEmitter(sseEmitter));
//        forRepo.setSseEmitter(sseEmitter);
//        sseEmitterRepository.save(forRepo);
        log.info("subscribed <<<");
//        return CompletableFuture.completedFuture(sseEmitter);
        return sseEmitter;
    }
    @Async
    @Override

    public void sendToRateNotification(Long raterID, Long toRateID, boolean raterIsMentor) {
        log.info("pushing {} notification for user {}", "notification for rating", raterID);
        Mentor mentor = null;
        Mentee mentee = null;
        if( raterIsMentor) {
            mentee = menteeRepository.findById(toRateID).orElseThrow(() -> new AccountNotFound(
                    "mentor with id - " + raterID));
        } else {
            mentor = mentorRepository.findById(toRateID).orElseThrow(() -> new AccountNotFound(
                    "mentee with id - " + raterID));
        }
        User user;
        if (raterIsMentor) {
            user = mentee.getUser();
        } else {
            user = mentor.getUser();
        }
        SSEEmitter sseEmitter = sseEmitterRepository.findByUser(user).orElse(new SSEEmitter(
                new SerializableSSE(24 * 60 * 60 * 1000L)));
        Mentee errorMentee = mentee;
        Mentor errorMentor = mentor;
        nonBlockingService.execute(() -> {
                    try {
                        sseEmitter.getSseEmitter().send(SseEmitter
                                .event()
                                .name("rating")
                                .data("rate mentor/mentee  with id: " + raterID));
                        sseEmitter.getSseEmitter().complete();
                    } catch (IOException e) {
                        sseEmitterRepository.delete(sseEmitter);
                        if (raterIsMentor) {
                            throw new EmitterGone("emitter of mentor - " + errorMentee.getUser().getEmail());
                        } else {
                            throw new EmitterGone("emitter of mentee - " + errorMentor.getUser().getEmail());
                        }
                    }
                });
        log.info("to rate notification was sent <<<");
    }

    @Async
    @Override
    public void sendNews(Long newsID) {
        log.info("sending news started ...");
        Post post = postRepository.findById(newsID).orElseThrow(() -> new AccountNotFound("post - " + newsID));
//        List<SSEEmitter> allEmitters = sseEmitterRepository.findAll();
        for (SseEmitter sseEmitter: list) {
            nonBlockingService.execute(() -> {
                try {
                    sseEmitter.send(SseEmitter
                            .event()
                            .id("1")
                            .comment("asjdnakjsdnas")
                            .name("news")
                            .data("check news with id: " + post.getId() + ". Post - " + post));
                    sseEmitter.complete();

                } catch (IOException e) {
//                    sseEmitterRepository.delete(sseEmitter);
                    list.remove(sseEmitter);
                }
            });
        }
        log.info("news were sent <<<");
    }
    @Async
    @Override
    public void sendSubscriptionNotification(Long mentorID, Long menteeID) {
        log.info("pushing {} notification for user {}", "\"subscription\"", mentorID);
        Mentor mentor = mentorRepository.findById(mentorID).orElseThrow(() -> new AccountNotFound(
                "mentor with id - " + mentorID));
        User user = mentor.getUser();
        SSEEmitter sseEmitter = sseEmitterRepository.findByUser(user).orElseThrow(() ->
                new AccountNotFound("emitter us user - " + user.getEmail()));
        nonBlockingService.execute(() -> {
                    try {
                        sseEmitter.getSseEmitter().send(SseEmitter
                                .event()
                                .id("1")
                                .comment("asjdnakjsdnas")
                                .name("subscription")
                                .data("you have new subscription of mentee with id: " + menteeID));
                        sseEmitter.getSseEmitter().complete();
                    } catch (IOException e) {
                        sseEmitterRepository.delete(sseEmitter);
                        throw new EmitterGone("emitter of mentor - " + mentor.getUser().getEmail());
                    }
                });
        log.info("subscription notification was sent <<<");
    }
    @Async
    @Override
    public void confirmationNotification(Long mentorID, Long menteeID) {
        log.info("pushing {} notification for user {}", "\"confirmation\"", mentorID);
        Mentee mentee = menteeRepository.findById(mentorID).orElseThrow(() -> new AccountNotFound(
                "mentor with id - " + menteeID));
        User user = mentee.getUser();
        SSEEmitter sseEmitter = sseEmitterRepository.findByUser(user).orElseThrow(() ->
                new AccountNotFound("emitter us user - " + user.getEmail()));
        nonBlockingService.execute(() -> {
                    try {
                        sseEmitter.getSseEmitter().send(SseEmitter
                                .event()
                                .id("1")
                                .comment("asjdnakjsdnas")
                                .name("confirmation")
                                .data("you have new confirmation from mentor with id: " + mentorID));
                        sseEmitter.getSseEmitter().complete();
                    } catch (IOException e) {
                        sseEmitterRepository.delete(sseEmitter);
                        throw new EmitterGone("emitter of mentor - " + mentee.getUser().getEmail());
                    }
                });
        log.info("confirmation notification was sent <<<");
    }
}
