package com.hackathon.mentor.service.serviceImpl;

import com.hackathon.mentor.exceptions.AccountConflict;
import com.hackathon.mentor.exceptions.AccountNotFound;
import com.hackathon.mentor.models.SSEEmitter;
import com.hackathon.mentor.models.SerializableSSE;
import com.hackathon.mentor.payload.request.NotificationRequest;
import com.hackathon.mentor.repository.SSEEmitterRepository;
import com.hackathon.mentor.service.EmitterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmitterServiceImpl implements EmitterService {
    private final SSEEmitterRepository sseEmitterRepository;
    public SSEEmitter addEmitter() {
        log.info("subscribing to notifications ...");
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        SerializableSSE sseEmitter = new SerializableSSE(24 * 60 * 60 * 1000L);
        SSEEmitter forRepo = sseEmitterRepository.findByUser_Email(email).orElse(new SSEEmitter(sseEmitter));
        forRepo.setSseEmitter(sseEmitter);
        forRepo.set
        SSEEmitter savedInRepo = sseEmitterRepository.save(forRepo);
        sseEmitter.onCompletion(() -> sseEmitterRepository.delete(savedInRepo));
        sseEmitter.onTimeout(() -> sseEmitterRepository.delete(savedInRepo));
        savedInRepo.setSseEmitter(sseEmitter);
        sseEmitterRepository.save(savedInRepo);
        log.info("subscribed <<<");
        return savedInRepo;
    }
    @Override
    public void sendToRateNotification(Long id, String topic, String message) {
        log.info("pushing {} notification for user {}", message, id);
        List<SSEEmitter> deadEmitters = new ArrayList<>();
        if (!Objects.equals(topic, "rating")) {
            throw new AccountConflict("wrong topic");
        }
        NotificationRequest payload = NotificationRequest
                .builder()
                .topic(topic)
                .message(message)
                .build();

        sseEmitterRepository.findAll().forEach(emitter -> {
            try {
                emitter.getSseEmitter().send(SseEmitter
                        .event()
                        .name(id.toString())
                        .data(payload));

            } catch (IOException e) {
                deadEmitters.add(emitter);
            }
        });
        sseEmitterRepository.deleteAll(deadEmitters);
    }

    @Override
    public SSEEmitter getEmitter(Long id) {
        return sseEmitterRepository.findById(id).orElse(null);
    }
}
