package com.hackathon.mentor.service;

import com.hackathon.mentor.models.SSEEmitter;

public interface EmitterService {
    void addEmitter();

    void sendToRateNotification(Long id, Long toRateID);

    SSEEmitter getEmitter(Long id);

    void sendNews(Long id);

    void sendSubscriptionNotification(Long id);
}
