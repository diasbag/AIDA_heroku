package com.hackathon.mentor.service.serviceImpl;

import com.hackathon.mentor.models.User;
import com.hackathon.mentor.payload.request.FeedbackRequest;
import com.hackathon.mentor.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@EnableAsync
public class FeedbackServiceImpl implements FeedbackService {
    private final JavaMailSender javaMailSender;
    @Async
    @Transactional
    @Override
    public void createFeedback(FeedbackRequest feedbackRequest)
            throws MessagingException, UnsupportedEncodingException {
        log.info("sending email with feedback started ...");
        String fromAddress = "diasbagzat2@gmail.com";
        String senderName = feedbackRequest.getEmail();
        String subject = "New feedback";
        String content = "Dear NIS Alumni,<br>"
                + "You have a new feedback:<br>"
                + feedbackRequest.getComment()
                + "Thank you,<br>";

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(fromAddress, senderName);
        helper.setSubject(subject);
        helper.setText(content, true);

        helper.setTo(feedbackRequest.getEmail());
        javaMailSender.send(message);
        log.info("email with feedback were sent <<<");
    }
}
