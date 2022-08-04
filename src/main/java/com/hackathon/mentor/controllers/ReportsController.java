package com.hackathon.mentor.controllers;

import com.hackathon.mentor.models.Report;
import com.hackathon.mentor.payload.request.ReportRequest;
import com.hackathon.mentor.payload.response.MessageResponse;
import com.hackathon.mentor.service.ReportsService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/reports")
@SecurityRequirement(name = "basicauth")
public class ReportsController {
    private final ReportsService reportsService;

    @PostMapping("/images_1")
    public ResponseEntity<?> uploadFiles(@RequestParam("files") MultipartFile[] files) {
        Report report = reportsService.uploadReportImages(files);
        return ResponseEntity.ok(report.getId());
    }
    @PostMapping("/report_user_2")
    public ResponseEntity<MessageResponse> reportUser(@RequestBody ReportRequest reportRequest)
            throws MessagingException, UnsupportedEncodingException {
        reportsService.reportPerson(reportRequest);
        return ResponseEntity.ok(new MessageResponse("User was reported"));
    }
    @GetMapping()
    public ResponseEntity<?> getReports() {
        return ResponseEntity.ok(reportsService.getReportsAll());
    }
    @GetMapping("non_ignored")
    public ResponseEntity<?> getNonIgnoredReports() {
        return ResponseEntity.ok(reportsService.getNonIgnoredReports());
    }
    @GetMapping("ignored")
    public ResponseEntity<?> getIgnoredReports() {
        return ResponseEntity.ok(reportsService.getIgnoredReports());
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getReportsByID(@PathVariable Long id) {
        return ResponseEntity.ok(reportsService.getReportById(id));
    }
    @GetMapping("/ignore")
    public ResponseEntity<?> ignoreByID(@RequestParam Long id) {
        reportsService.reportIgnore(id);
        return ResponseEntity.ok("Report was marked as \"ignore\"");
    }

    @PutMapping("/edit_report")
    public ResponseEntity<?> editReport(@RequestBody ReportRequest reportRequest) {
        reportsService.editReport(reportRequest);
        return ResponseEntity.ok("Report was edited");
    }
    @PutMapping("/edit_images/{id}")
    public ResponseEntity<?> editImages(@PathVariable Long id, @RequestParam("files") MultipartFile[] files) {
        reportsService.editImages(id, files);
        return ResponseEntity.ok("Images were edited");
    }
}
