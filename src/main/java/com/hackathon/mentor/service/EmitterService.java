package com.hackathon.mentor.service;

import com.hackathon.mentor.models.SerializableSSE;

public interface EmitterService {
    SerializableSSE addEmitter(String email);

    void sendToRateNotification(Long raterID, Long toRateID, boolean raterIsMentor);

    void sendNews(Long newsID);

    void sendSubscriptionNotification(Long mentorID, Long menteeID);

    void confirmationNotification(Long mentorID, Long menteeID);
}
