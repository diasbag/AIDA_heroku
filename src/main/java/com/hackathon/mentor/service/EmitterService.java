package com.hackathon.mentor.service;

import com.hackathon.mentor.models.SerializableSSE;

import java.util.concurrent.CompletableFuture;

public interface EmitterService {
    CompletableFuture<SerializableSSE> addEmitter(String email);

    void sendToRateNotification(Long raterID, Long toRateID, boolean raterIsMentor);

    void sendNews(Long newsID);

    void sendSubscriptionNotification(Long mentorID, Long menteeID);

    void confirmationNotification(Long mentorID, Long menteeID);
}
