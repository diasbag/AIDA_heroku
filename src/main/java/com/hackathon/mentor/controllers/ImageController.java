package com.hackathon.mentor.controllers;

import com.hackathon.mentor.models.Image;
import com.hackathon.mentor.models.User;
import com.hackathon.mentor.repository.UserRepository;
import com.hackathon.mentor.services.ImageService;
import com.hackathon.mentor.utils.FileNameHelper;
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
public class ImageController {

    @Autowired
    private ImageService imageService;

    @Autowired
    UserRepository userRepository;


    private FileNameHelper fileHelper = new FileNameHelper();

    @PostMapping("/upload")
    public ResponseEntity<?> uploadSingleFile(@RequestParam("file") MultipartFile file) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        return imageService.uploadFile(email, file);
    }

    @GetMapping("/show/user/avatar")
    public ResponseEntity<byte[]> getImage() throws Exception {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        return imageService.getAvatar(email);
    }
}
