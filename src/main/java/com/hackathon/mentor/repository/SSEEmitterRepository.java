package com.hackathon.mentor.repository;

import com.hackathon.mentor.models.SSEEmitter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SSEEmitterRepository extends JpaRepository<SSEEmitter, Long> {
}
