package com.hackathon.mentor.service;

import com.hackathon.mentor.models.Report;
import com.hackathon.mentor.payload.request.ReportRequest;
import org.springframework.scheduling.annotation.Async;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface ReportsService {
    @Transactional
    void reportPerson(ReportRequest reportRequest);

    @Transactional
    @Async
    void sendingNotificationReport()
            throws MessagingException, UnsupportedEncodingException;

    void reportIgnore(Long reportId);

    @Transactional
    Report getReportById(Long reportId);

    List<Long> getReportsAll();
}
