package com.hackathon.mentor.repository;

import com.hackathon.mentor.models.SSEEmitter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SSEEmitterRepository extends JpaRepository<SSEEmitter, Long> {
    Optional<SSEEmitter> findByUser_Email(String email);
}
