package com.hackathon.mentor.security.services;

import com.hackathon.mentor.models.Image;
import com.hackathon.mentor.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;

    public ResponseEntity<?> save(Image image) {
        if (image == null) {
            throw new NullPointerException("Image data NULL!!!!");

        }
        imageRepository.save(image);
        return new ResponseEntity<>("image", HttpStatus.OK);
    }

    public Image findByFileName(String fileName) {
        return this.imageRepository.findByFileName(fileName);
    }

    public Image findByUuid(String uuid) {
        return this.imageRepository.findByUuid(uuid);
    }
}
