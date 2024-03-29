package com.hackathon.mentor.service;

import com.hackathon.mentor.models.Image;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    ResponseEntity<?> save(Image image);

    Image findByFileName(String fileName);

    ResponseEntity<?> uploadSingleFile(String email, MultipartFile file);

    Image getImage(String email);
    Image findByUuid(String uuid);

    Image getImageByID(Long id);

    void fakeImages(MultipartFile[] files);
}
