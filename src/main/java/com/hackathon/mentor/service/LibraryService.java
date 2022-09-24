package com.hackathon.mentor.service;

import com.hackathon.mentor.models.FileEntity;
import com.hackathon.mentor.payload.request.LibraryTextRequest;
import com.hackathon.mentor.payload.response.TextResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface LibraryService {

    String save(MultipartFile file) throws IOException;

    FileEntity getFile(String id);

    void postText(LibraryTextRequest libraryTextRequest);

    List<TextResponse> getText();
}
