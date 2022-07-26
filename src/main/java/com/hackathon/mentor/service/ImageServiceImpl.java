package com.hackathon.mentor.service;

import com.hackathon.mentor.models.Image;
import com.hackathon.mentor.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;

    @Override
    public ResponseEntity<?> save(Image image) {
        if (image == null) {
            throw new NullPointerException("Image data NULL!!!!");

        }
        imageRepository.save(image);
        return new ResponseEntity<>("image", HttpStatus.OK);
    }

    @Override
    public Image findByFileName(String fileName) {
        return this.imageRepository.findByFileName(fileName);
    }
    @Override
    public Image findByUuid(String uuid) {
        return this.imageRepository.findByUuid(uuid);
    }
}
