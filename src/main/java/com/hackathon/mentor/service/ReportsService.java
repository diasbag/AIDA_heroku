package com.hackathon.mentor.service;

import com.hackathon.mentor.models.Report;
import com.hackathon.mentor.payload.request.ReportRequest;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface ReportsService {
    @Transactional
    void reportPerson(ReportRequest reportRequest) throws MessagingException, UnsupportedEncodingException;

    void reportIgnore(Long reportId);

    @Transactional
    Report getReportById(Long reportId);

    List<Report> getReportsAll();
}
