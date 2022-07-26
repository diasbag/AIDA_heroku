package com.hackathon.mentor.services;

import com.hackathon.mentor.models.Image;
import com.hackathon.mentor.models.User;
import com.hackathon.mentor.repository.ImageRepository;
import com.hackathon.mentor.repository.UserRepository;
import com.hackathon.mentor.utils.FileNameHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private UserRepository userRepository;

    private FileNameHelper fileHelper = new FileNameHelper();

    public ResponseEntity<?> save(Image image) {
        if (image == null) {
            throw new NullPointerException("Image data NULL!!!!");

        }
        imageRepository.save(image);
        return new ResponseEntity<>("image", HttpStatus.OK);
    }

    public ResponseEntity<?> uploadFile(String email, MultipartFile file) {
        User user = userRepository.findByEmail(email).orElse(null);
        Image image = Image.buildImage(file, fileHelper);
        user.setImage(image);
        userRepository.save(user);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
    public Image findByFileName(String fileName) {
        return this.imageRepository.findByFileName(fileName);
    }

    public ResponseEntity<byte[]> getAvatar(String email) {
        User user = userRepository.getByEmail(email);
        String fileName = user.getImage().getFileName();
        Image image = imageRepository.findByFileName(fileName);
        return ResponseEntity.ok().contentType(MediaType.valueOf(image.getFileType())).body(image.getData());
    }
    public Image findByUuid(String uuid) {
        return this.imageRepository.findByUuid(uuid);
    }
}
