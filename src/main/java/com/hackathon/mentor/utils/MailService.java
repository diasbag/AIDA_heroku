package com.hackathon.mentor.utils;

import com.hackathon.mentor.models.User;
import com.hackathon.mentor.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender mailSender;
    private final AdminService adminService;

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

    public void sendingNotificationReport() throws MessagingException, UnsupportedEncodingException {
        log.info("sending email started ...");
        List<User> listOfAdmins = adminService.findAllAdmins();
        String fromAddress = "test.spring.test@mail.ru";
        String senderName = "Mentorship Alumni NIS.";
        String subject = "You've got a new report";
        String content = "Dear [[name]],<br>"
                + "You have a new report to be judged:<br>"
                + "Thank you,<br>"
                + "Mentorship Alumni NIS.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(fromAddress, senderName);
        helper.setSubject(subject);
        helper.setText(content, true);

        for(User admin:listOfAdmins){
            String toAddress = admin.getEmail();
            helper.setTo(toAddress);
            mailSender.send(message);
        }
        log.info("emails with report notifications were sent <<<");
    }
}
