package com.hackathon.mentor.controllers;

import com.hackathon.mentor.models.Image;
import com.hackathon.mentor.repository.UserRepository;
import com.hackathon.mentor.service.serviceImpl.ImageServiceImpl;
import com.hackathon.mentor.utils.FileNameHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ImageController {

    private final ImageServiceImpl imageServiceImpl;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadSingleFile(@RequestParam("file") MultipartFile file) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        return imageServiceImpl.uploadSingleFile(email, file);
    }

    @GetMapping("/show/user/avatar")
    public ResponseEntity<byte[]> getImage() throws Exception {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        Image image = imageServiceImpl.getImage(email);
        return ResponseEntity.ok().contentType(MediaType.valueOf(image.getFileType())).body(image.getData());
    }
    @GetMapping("/show/user/avatar_by_id")
    public ResponseEntity<byte[]> getImagebyId(@RequestParam Long id) throws Exception {
        Image image = imageServiceImpl.getImageByID(id);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf(image.getFileType())).body(image.getData());
    }
    @PostMapping("/fake_images")
    public ResponseEntity<?> fakeImages(@RequestParam("file") MultipartFile[] files) throws Exception {
        imageServiceImpl.fakeImages(files);
        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }

}
