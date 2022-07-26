package com.hackathon.mentor.service;

import com.hackathon.mentor.models.Image;
import org.springframework.http.ResponseEntity;

public interface ImageService {
    ResponseEntity<?> save(Image image);

    Image findByFileName(String fileName);

    Image findByUuid(String uuid);
}
