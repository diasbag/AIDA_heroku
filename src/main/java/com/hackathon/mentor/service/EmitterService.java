package com.hackathon.mentor.service;

import com.hackathon.mentor.models.SSEEmitter;
import com.hackathon.mentor.models.SerializableSSE;

public interface EmitterService {
    SSEEmitter addEmitter();

    void sendToRateNotification(Long id, String name, String message);

    SSEEmitter getEmitter(Long id);
}
