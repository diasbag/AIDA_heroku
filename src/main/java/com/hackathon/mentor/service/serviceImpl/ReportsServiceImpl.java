package com.hackathon.mentor.service.serviceImpl;

import com.hackathon.mentor.exceptions.AccountNotFound;
import com.hackathon.mentor.models.Report;
import com.hackathon.mentor.models.User;
import com.hackathon.mentor.payload.request.ReportRequest;
import com.hackathon.mentor.repository.ReportRepository;
import com.hackathon.mentor.repository.UserRepository;
import com.hackathon.mentor.service.AdminService;
import com.hackathon.mentor.service.ReportsService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@EnableAsync
public class ReportsServiceImpl implements ReportsService {

    private final  JavaMailSender mailSender;
    private final AdminService adminService;
    private final  UserRepository userRepository;
    private final  ReportRepository reportRepository;


    @Override
    @Transactional
    public void reportPerson(ReportRequest reportRequest) {
        log.info("reporting harasser ...");
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new AccountNotFound("reporting user not found"));
        userRepository.findById(reportRequest.getHarasserId()).orElseThrow(() ->
                new AccountNotFound("harasser not found"));
        Report newReport = Report.builder()
                .ignore(false)
                .reason(reportRequest.getReason())
                .reporterID(user.getId())
                .harasserID(reportRequest.getHarasserId())
                .build();
        reportRepository.save(newReport);
        log.info("report is saved <<<");
    }

    @Override
    @Transactional
    @Async
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

    @Override
    public void reportIgnore(Long reportId){
        log.info("ignoring report ...");
        Report report = getReportById(reportId);
        report.setIgnore(true);
        reportRepository.save(report);
        log.info(reportId + "report is ignored <<<");
    }

    @Override
    @Transactional
    public Report getReportById(Long reportId){
        log.info("getting report by id ...");
        Report report = reportRepository.findById(reportId).orElseThrow(() ->
                new AccountNotFound("report with id: " + reportId));
        log.info("report was successfully retrieved by id <<<");
        return report;
    }

    @Override
    public List<Long> getReportsAll(){
        log.info("accessing database for all reports ...");
        List<Report> allReports = reportRepository.findAll();
        List<Long> listById = new ArrayList<>();
        for( Report report : allReports){
            listById.add(report.getId());
        }
        log.info("reports are retrieved <<<");
        return listById;
    }

}
