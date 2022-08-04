package com.hackathon.mentor.service.serviceImpl;

import com.hackathon.mentor.exceptions.AccountConflict;
import com.hackathon.mentor.exceptions.AccountNotFound;
import com.hackathon.mentor.exceptions.ServerFail;
import com.hackathon.mentor.models.*;
import com.hackathon.mentor.payload.request.ReportRequest;
import com.hackathon.mentor.repository.*;
import com.hackathon.mentor.service.ReportsService;
import com.hackathon.mentor.utils.FileNameHelper;
import com.hackathon.mentor.utils.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@EnableAsync
public class ReportsServiceImpl implements ReportsService {
    private final MailService mailService;
    private final  UserRepository userRepository;
    private final  ReportRepository reportRepository;
    private final MentorRepository mentorRepository;
    private final MenteeRepository menteeRepository;
    private final ImageRepository imageRepository;
    private final FileNameHelper fileHelper = new FileNameHelper();

    @Override
    public void reportPerson(ReportRequest reportRequest) throws MessagingException, UnsupportedEncodingException {
        log.info("reporting harasser ...");
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails;
        if (principal instanceof String) {
            throw new AuthenticationFailedException("user is not authenticated");
        } else {
            userDetails = (UserDetails) principal;
        }
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new AccountNotFound("reporting user not found"));
        Mentor mentor = null;
        Mentee mentee = null;
        if (user.getRoles().get(0).getName() == ERole.ROLE_MENTOR) {
            mentor = mentorRepository.findByUser(user).orElseThrow(() -> new AccountNotFound("mentor - " + user));
        } else if (user.getRoles().get(0).getName() == ERole.ROLE_MENTEE) {
            mentee = menteeRepository.findByUser(user).orElseThrow(() -> new AccountNotFound("mentee - " + user));
        } else {
            throw new AccountConflict("wrong role");
        }
        Report report;
        if (reportRequest.getId() == null || !reportRepository.findById(reportRequest.getId()).isPresent()) {
            report = new Report();
        } else {
            report = reportRepository.findById(reportRequest.getId()).orElseThrow(() -> new AccountNotFound(
                    "this will never happen"));
        }
        if (report.getReason() != null) {
            throw new AccountConflict("report is already in database");
        }
        Long id;
        if (mentor != null) {
            id = mentor.getId();
        } else if (mentee != null){
            id = mentee.getId();
        } else {
            throw new ServerFail("somehow both mentor and mentee are null");
        }
        if (reportRepository.findReportByReporterIDAndHarasserID(id, reportRequest.getHarasserId()).isPresent()) {
            throw new AccountConflict("user already reported this person");
        }
        report.setReporterID(id);
        report.setReportDate(Date.from(Instant.now()));
        report.setHarasserID(reportRequest.getHarasserId());
        report.setIgnore(false);
        report.setReason(reportRequest.getReason());
        reportRepository.save(report);
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

    @Override
    public Report uploadReportImages(MultipartFile[] files) {
        log.info("uploading post image ...");
        Report report = new Report();
        List<Image> reportImages = new ArrayList<>();
        for (MultipartFile file: files) {
            Image image = Image.buildImage(file, fileHelper);
            reportImages.add(image);
            imageRepository.save(image);
        }
        report.setImages(reportImages);
        reportRepository.save(report);
        log.info("photo was uploaded <<<");
        return report;
    }

    @Override
    public List<Report> getNonIgnoredReports() {
        log.info("accessing database for all reports ...");
        List<Report> allReports = reportRepository.findAllByIgnoreIsFalse();
        log.info("reports are retrieved <<<");
        return allReports;
    }

    @Override
    public List<Report> getIgnoredReports() {
        log.info("accessing database for all reports ...");
        List<Report> allReports = reportRepository.findAllByIgnoreIsTrue();
        log.info("reports are retrieved <<<");
        return allReports;
    }

    @Override
    public void editReport(ReportRequest reportRequest) {
        log.info("starting report edit ...");
        Report report = reportRepository.findById(reportRequest.getId()).orElseThrow(() -> new
                AccountNotFound("report with id - " + reportRequest.getId()));
        if (report.getIgnore()) {throw new AccountConflict("report is marked as ignore");}
        report.setReason(reportRequest.getReason());
        report.setReportDate(Date.from(Instant.now()));
        reportRepository.save(report);
        log.info("report was edited - " + report);
    }

    @Override
    public void editImages(Long id, MultipartFile[] files) {
        log.info("starting report images edit ...");
        Report report = reportRepository.findById(id).orElseThrow(() -> new AccountNotFound("report with id - "
                + id));
        if (report.getIgnore()) {throw new AccountConflict("report is marked as ignore");}
        if(!report.getImages().isEmpty() || report.getImages() != null) {
            imageRepository.deleteAll(report.getImages());
        }
        List<Image> reportImages = new ArrayList<>();
        for (MultipartFile file: files) {
            Image image = Image.buildImage(file, fileHelper);
            reportImages.add(image);
            imageRepository.save(image);
        }

        report.setImages(reportImages);
        report.setReportDate(Date.from(Instant.now()));
        reportRepository.save(report);
        log.info("report images were edited - " + report);
    }
}
