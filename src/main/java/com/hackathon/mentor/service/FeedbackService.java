package com.hackathon.mentor.service;

import com.hackathon.mentor.payload.request.FeedbackRequest;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface FeedbackService {
    void createFeedback(FeedbackRequest feedbackRequest) throws MessagingException, UnsupportedEncodingException;
}
