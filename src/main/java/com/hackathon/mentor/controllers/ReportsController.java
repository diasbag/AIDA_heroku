package com.hackathon.mentor.controllers;

import com.hackathon.mentor.models.Image;
import com.hackathon.mentor.payload.request.ReportRequest;
import com.hackathon.mentor.payload.response.MessageResponse;
import com.hackathon.mentor.service.ImageService;
import com.hackathon.mentor.service.ReportsService;
import com.hackathon.mentor.utils.FileNameHelper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/report")
@SecurityRequirement(name = "basicauth")
public class ReportsController {
    private final ImageService imageService;
    private final ReportsService reportsService;
    private final FileNameHelper fileHelper = new FileNameHelper();

    @PostMapping("/images")
    public ResponseEntity<MessageResponse> uploadFiles(@RequestParam("files") MultipartFile[] files) {
        String message = "";
        try {
            List<String> fileNames = new ArrayList<>();
            Arrays.stream(files).forEach(file -> {
                Image image = Image.buildImage(file, fileHelper);
                imageService.save(image);
                fileNames.add(file.getOriginalFilename());
            });
            message = "Uploaded the files successfully: " + fileNames;
            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(message));
        } catch (Exception e) {
            message = "Fail to upload files!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse(message));
        }
    }
    @PostMapping("/report_user")
    public ResponseEntity<MessageResponse> reportUser(@RequestBody ReportRequest reportRequest) {
        reportsService.reportPerson(reportRequest);
        return ResponseEntity.ok(new MessageResponse("User was reported"));
    }
}
