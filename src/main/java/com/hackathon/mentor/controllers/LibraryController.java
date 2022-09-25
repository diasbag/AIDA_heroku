package com.hackathon.mentor.controllers;

import com.hackathon.mentor.models.FileEntity;
import com.hackathon.mentor.payload.request.LibraryTextRequest;
import com.hackathon.mentor.payload.response.TextResponse;
import com.hackathon.mentor.service.LibraryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(value = "*", maxAge = 3600)
@RequiredArgsConstructor
@RequestMapping("/library")
@SecurityRequirement(name = "basicauth")
public class LibraryController {
    private final LibraryService libraryService;

    @PostMapping("/file/{id}")
    public ResponseEntity<?> saveFile(@RequestParam("file") MultipartFile file, @PathVariable Long id) {
        try {
            String fileId = libraryService.save(file, id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(String.format("File uploaded successfully: %s", fileId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(String.format("Could not upload the file: %s!", file.getOriginalFilename()));
        }
    }

    @GetMapping("/file/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable String id) {
        FileEntity fileEntity =  libraryService.getFile(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileEntity.getName() + "\"")
                .contentType(MediaType.valueOf(fileEntity.getContentType()))
                .body(fileEntity.getData());
    }

    @PostMapping("/text/")
    public ResponseEntity<?> postText(@RequestBody LibraryTextRequest libraryTextRequest) {
        libraryService.postText(libraryTextRequest);
        return ResponseEntity.ok(">>> text was posted");
    }

    @GetMapping("/text/")
    public ResponseEntity<?> getText() {
        List<TextResponse> out = libraryService.getText();
        return ResponseEntity.ok("{\"library\": " + out);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteFile(@PathVariable String id) {
        libraryService.deleteFIle(id);
        return ResponseEntity.ok("file was deleted");
    }

    @PutMapping("/text")
    public ResponseEntity<?> editText(@RequestBody LibraryTextRequest libraryTextRequest) {
        libraryService.editText(libraryTextRequest);
        return ResponseEntity.ok("text was changed");
    }
    @PutMapping("/file/{id}")
    public ResponseEntity<?> editFile(@RequestParam("file") MultipartFile file, @PathVariable String id)
            throws IOException {
        libraryService.editFile(file, id);
        return ResponseEntity.ok("file was changed");
    }
}
