package com.hackathon.mentor.controllers;

import com.hackathon.mentor.models.Image;
import com.hackathon.mentor.models.User;
import com.hackathon.mentor.repository.UserRepository;
import com.hackathon.mentor.security.services.ImageService;
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
    public ResponseEntity<?> uploadSingleFile(@RequestParam(value = "file") MultipartFile file) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email).orElse(null);
        Image image = Image.buildImage(file, fileHelper);
        user.setImage(image);
        userRepository.save(user);
        return new ResponseEntity<>("success" , HttpStatus.OK);
    }

    @GetMapping("/show/user/avatar")
    public ResponseEntity<byte[]> getImage() throws Exception {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        User user = userRepository.getByEmail(email);
        String fileName = user.getImage().getFileName();
        Image image = imageService.findByFileName(fileName);
        return ResponseEntity.ok().contentType(MediaType.valueOf(image.getFileType())).body(image.getData());
    }
}
