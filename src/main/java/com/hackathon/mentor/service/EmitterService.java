package com.hackathon.mentor.service;

import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface EmitterService {
    SseEmitter addEmitter(String email);

    void sendToRateNotification(Long raterID, Long toRateID, boolean raterIsMentor);

    void sendNews(Long newsID);

    void sendSubscriptionNotification(Long mentorID, Long menteeID);

    void confirmationNotification(Long mentorID, Long menteeID);
}
