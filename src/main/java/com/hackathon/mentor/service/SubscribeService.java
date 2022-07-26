package com.hackathon.mentor.service;

import org.springframework.http.ResponseEntity;

public interface SubscribeService {

    ResponseEntity<?> getSubscribers();

    ResponseEntity<?> subscribe(Long id, String email);
}
