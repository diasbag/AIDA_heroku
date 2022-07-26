package com.hackathon.mentor.service.serviceImpl;

import com.hackathon.mentor.exceptions.AccountNotFound;
import com.hackathon.mentor.models.Image;
import com.hackathon.mentor.models.User;
import com.hackathon.mentor.repository.ImageRepository;
import com.hackathon.mentor.repository.UserRepository;
import com.hackathon.mentor.service.ImageService;
import com.hackathon.mentor.utils.FileNameHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;

    private final UserRepository userRepository;

    private final FileNameHelper fileHelper = new FileNameHelper();

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
    public ResponseEntity<?> uploadSingleFile(String email, MultipartFile file) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AccountNotFound("image not found"));
        log.info("uploading image...");
        Image image = Image.buildImage(file, fileHelper);
        user.setImage(image);
        userRepository.save(user);
        log.info("image successfully uploaded");
        return new ResponseEntity<>("success" , HttpStatus.OK);
    }

    @Override
    public ResponseEntity<byte[]> getImage(String email) {
        User user = userRepository.getByEmail(email);
        log.info("show image...");
        String fileName = user.getImage().getFileName();
        Image image = findByFileName(fileName);
        log.info("success");
        return ResponseEntity.ok().contentType(MediaType.valueOf(image.getFileType())).body(image.getData());
    }

    @Override
    public Image findByUuid(String uuid) {
        return this.imageRepository.findByUuid(uuid);
    }
}
