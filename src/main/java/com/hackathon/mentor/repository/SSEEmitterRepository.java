package com.hackathon.mentor.repository;

import com.hackathon.mentor.models.SSEEmitter;
import com.hackathon.mentor.models.SerializableSSE;
import com.hackathon.mentor.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SSEEmitterRepository extends JpaRepository<SSEEmitter, Long> {
    Optional<SSEEmitter> findByUser(User user);
    void deleteBySseEmitter(SerializableSSE sseEmitter);
}
