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
        log.info("saving image ...");
        if (image == null) {
            throw new NullPointerException("Image data NULL!!!!");
        }
        imageRepository.save(image);
        log.info("image was saved <<<");
        return new ResponseEntity<>("image", HttpStatus.OK);
    }

    @Override
    public Image findByFileName(String fileName) {
        log.info("finding image by filename ...");
        Image image = imageRepository.findByFileName(fileName).orElseThrow(() -> new AccountNotFound("image"));
        log.info("image was found <<<");
        return image;
    }

    @Override
    public ResponseEntity<?> uploadSingleFile(String email, MultipartFile file) {
        log.info("uploading image ...");
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AccountNotFound("image not found"));
        Image image = Image.buildImage(file, fileHelper);
        user.setImage(image);
        userRepository.save(user);
        log.info("image successfully uploaded");
        log.info("image saved");
        return new ResponseEntity<>("success" , HttpStatus.OK);
    }

    @Override
    public Image getImage(String email) {
        log.info("getting image ...");
        User user = userRepository.getByEmail(email);
        log.info("show image...");
        String fileName = user.getImage().getFileName();
        Image image = findByFileName(fileName);
        return image;
    }

    @Override
    public Image findByUuid(String uuid) {
        return this.imageRepository.findByUuid(uuid);
    }

    @Override
    public Image getImageByID(Long id) {
        log.info("getting image by id ...");
        User user = userRepository.findById(id).orElseThrow(() -> new AccountNotFound("user with id" + id));
        String fileName = user.getImage().getFileName();
        log.info("image search by id was completed <<<");
        return findByFileName(fileName);
    }
}
