package com.hackathon.mentor.service.serviceImpl;

import com.hackathon.mentor.exceptions.AccountNotFound;
import com.hackathon.mentor.models.Report;
import com.hackathon.mentor.models.User;
import com.hackathon.mentor.payload.request.ReportRequest;
import com.hackathon.mentor.repository.ReportRepository;
import com.hackathon.mentor.repository.UserRepository;
import com.hackathon.mentor.service.ReportsService;
import com.hackathon.mentor.utils.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.time.Instant;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@EnableAsync
public class ReportsServiceImpl implements ReportsService {

    private final MailService mailService;
    private final  UserRepository userRepository;
    private final  ReportRepository reportRepository;


    @Override
    @Transactional
    public void reportPerson(ReportRequest reportRequest) throws MessagingException, UnsupportedEncodingException {
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
                .reportDate(Date.from(Instant.now()))
                .build();
        reportRepository.save(newReport);
        mailService.sendingNotificationReport();
        log.info("report is saved <<<");
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
    public List<Report> getReportsAll(){
        log.info("accessing database for all reports ...");
        List<Report> allReports = reportRepository.findAll();
        log.info("reports are retrieved <<<");
        return allReports;
    }

}
