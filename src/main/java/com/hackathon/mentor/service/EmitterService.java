package com.hackathon.mentor.service;

import com.hackathon.mentor.models.SerializableSSE;

public interface EmitterService {
    SerializableSSE addEmitter();

    void sendToRateNotification(Long id, Long toRateID);

    void sendNews(Long newsID);

    void sendSubscriptionNotification(Long mentorID, Long menteeID);

    void confirmationNotification(Long mentorID, Long menteeID);
}
