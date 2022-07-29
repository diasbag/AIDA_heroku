package com.hackathon.mentor.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender mailSender;

    private final String from = "diasbagzat2@gmail.com";
    public void sendSubscribeMail(String to, String firstname, String lastname){
        //String to = "diasbagzat@gmail.com";
        log.info("sending message to Mentor...");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject("Запрос на менторство!!!");
        message.setText("Ментии: " + firstname + " " + lastname + " хочет подписаться на вас!!!");

        mailSender.send(message);
        log.info("Vse normal'no rabotaet huli noesh'");
    }

    public void sendSubscribeMailToMentee(String to, String firstname, String lastname) {
        log.info("sending message to Mentee...");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject("Запрос на менторство!!!");
        message.setText("Вы в списке ожидания у ментора: " + firstname + " " + lastname + ".");

        mailSender.send(message);
        log.info("Vse normal'no rabotaet huli noesh'");
    }
}
