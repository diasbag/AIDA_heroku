package com.hackathon.mentor.service.serviceImpl;

import com.hackathon.mentor.exceptions.AccountNotFound;
import com.hackathon.mentor.exceptions.FileEmpty;
import com.hackathon.mentor.models.FileEntity;
import com.hackathon.mentor.models.Mentee;
import com.hackathon.mentor.models.Mentor;
import com.hackathon.mentor.models.User;
import com.hackathon.mentor.payload.request.LibraryTextRequest;
import com.hackathon.mentor.payload.response.TextResponse;
import com.hackathon.mentor.repository.FileRepository;
import com.hackathon.mentor.repository.MenteeRepository;
import com.hackathon.mentor.repository.MentorRepository;
import com.hackathon.mentor.repository.UserRepository;
import com.hackathon.mentor.service.LibraryService;
import com.hackathon.mentor.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.sql.Date;
import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class LibraryServiceImpl implements LibraryService {
    private final FileRepository fileRepository;
    private final UserRepository userRepository;
    private final MentorRepository mentorRepository;
    private final MenteeRepository menteeRepository;

    @Transactional
    @Override
    public String save(MultipartFile file, Long id) throws IOException {
        log.info("started uploading file ...");
        if (file == null) {
            throw new FileEmpty("upload date - " + Date.from(Instant.now()));
        }
        FileEntity fileEntity = new FileEntity();
        fileEntity.setName(StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename())));
        fileEntity.setContentType(file.getContentType());
        fileEntity.setData(file.getBytes());
        fileEntity.setSize(file.getSize());
        String email = Utils.getEmail();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AccountNotFound("user - " + email));
        Mentor mentor;
        Mentee mentee;
        if (mentorRepository.findByUser(user).isPresent()) {
            mentor = mentorRepository.getByUser(user);
            mentee = menteeRepository.findById(id).orElseThrow(() -> new AccountNotFound(
                    "mentee with id - " + id));
        } else if (menteeRepository.findByUser(user).isPresent()) {
            mentee = menteeRepository.findByUser(user).get();
            mentor = mentorRepository.findById(id).orElseThrow(() -> new AccountNotFound(
                    "mentee with id - " + id));
        } else {
            throw new AccountNotFound("mentor or mentee with id - " + email);
        }
        fileEntity.setMentee(mentee);
        fileEntity.setMentor(mentor);
        FileEntity fileSaved = fileRepository.save(fileEntity);
        log.info("file was saved <<<");
        return fileSaved.getId();
    }
    @Transactional
    @Override
    public FileEntity getFile(String id) {
        log.info("finding file  - " + id + " ...");
        FileEntity out = fileRepository.findById(id).orElseThrow(() -> new AccountNotFound(
                "file with id - " + id));
        log.info("file was retrieved <<<");
        return out;
    }
//    @Override
//    public List<FileResponse> getAllFiles() {
//        log.info("retrieving files ...");
//        List<FileResponse> out = fileRepository.findAll()
//                .stream()
//                .map(this::mapToFileResponse)
//                .collect(Collectors.toList());
//        log.info("files were retrieved <<<");
//        return out;
//    }
//    private FileResponse mapToFileResponse(FileEntity fileEntity) {
//        log.info("mapping files to response started ...");
//        String downloadURL = ServletUriComponentsBuilder.fromCurrentContextPath()
//                .path("/files/")
//                .path(fileEntity.getId())
//                .toUriString();
//        FileResponse fileResponse = new FileResponse();
//        fileResponse.setId(fileEntity.getId());
//        fileResponse.setName(fileEntity.getName());
//        fileResponse.setContentType(fileEntity.getContentType());
//        fileResponse.setSize(fileEntity.getSize());
//        fileResponse.setUrl(downloadURL);
//        log.info("mapping files finished <<<");
//        return fileResponse;
//    }

    @Transactional
    @Override
    public void postText(LibraryTextRequest libraryTextRequest) {
        log.info("posting text of the file with id - " + libraryTextRequest.getId() + " ...");
        String email = Utils.getEmail();
        User user =  userRepository.findByEmail(email).orElseThrow(() -> {
                    throw new AccountNotFound(
                            "user with email" + email);
                });
        String id = libraryTextRequest.getId();
        List<FileEntity> files;
        if (mentorRepository.findByUser(user).isPresent()) {
            Long mentorId = mentorRepository.findByUser(user).get().getId();
            files = fileRepository.findByMentor_Id(mentorId);
            for (FileEntity file: files) {
                if (file.getId().equals(id)) {
                    file.setText(libraryTextRequest.getText());
                }
            }
        } else if (mentorRepository.findByUser(user).isPresent()) {
            Long menteeId = mentorRepository.findByUser(user).get().getId();
            files = fileRepository.findByMentee_Id(menteeId);
            for (FileEntity file: files) {
                if (file.getId().equals(id)) {
                    file.setText(libraryTextRequest.getText());
                }
            }
        } else {
            throw new AccountNotFound(" file by user details - " + email);
        }
        log.info("text of the file with id - " + libraryTextRequest.getId() + " was saved <<<");
    }

    @Transactional
    @Override
    public List<TextResponse> getText() {
        String email = Utils.getEmail();
        log.info("getting all files with of user with email - " + email + " ...");
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AccountNotFound(
                "user with email - " + email));
        HashMap<Long, TextResponse> uniques = new HashMap<>();
        if (mentorRepository.findByUser(user).isPresent()) {
            Mentor mentor = mentorRepository.getByUser(user);
            List<FileEntity> files = fileRepository.findByMentor_Id(mentor.getId());
            Boolean isMentor = true;
            textResponseListBuilder(uniques, files, isMentor);
        } else if (menteeRepository.findByUser(user).isPresent()) {
            Mentee mentee = menteeRepository.findByUser(user).get();
            List<FileEntity> files = fileRepository.findByMentee_Id(mentee.getId());
            Boolean isMentor = false;
            textResponseListBuilder(uniques, files, isMentor);
        } else {
            throw new AccountNotFound("user with email - " + email);
        }
        log.info("all files with of user with email - " + email + " were retrieved <<<");
        return new ArrayList<>(uniques.values());
    }
    @Transactional
    @Override
    public void deleteFIle(String id) {
        log.info("deleting file with id - " + id + " ...");
        fileRepository.findById(id).orElseThrow(() -> new AccountNotFound("file with id - " + id));
        fileRepository.deleteById(id);
        log.info("file with id - " + id + " was deleted <<<");
    }

    @Override
    public void editText(LibraryTextRequest libraryTextRequest) {
        log.info("editing text of the file with id - " + libraryTextRequest.getId() + " ...");
        String id = libraryTextRequest.getId();
        FileEntity file = fileRepository.findById(id).orElseThrow(() -> new AccountNotFound(
                "file with id - " + id));
        file.setText(libraryTextRequest.getText());
        fileRepository.save(file);
        log.info("editing text of the file with id - " + libraryTextRequest.getId() + " was finished <<<");
    }

    @Override
    public void editFile(MultipartFile file, String id) throws IOException {
        log.info("started changing the file ...");
        if (file == null) {
            throw new FileEmpty("upload date - " + Date.from(Instant.now()));
        }
        FileEntity fileEntity = fileRepository.findById(id).orElseThrow(() -> new AccountNotFound(
                "file with id - " + id));
        fileEntity.setName(StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename())));
        fileEntity.setContentType(file.getContentType());
        fileEntity.setData(file.getBytes());
        fileEntity.setSize(file.getSize());
        fileRepository.save(fileEntity);
        log.info("file was saved <<<");
    }

    private void textResponseListBuilder(HashMap<Long, TextResponse> uniques, List<FileEntity> files,
                                         Boolean isMentor) {
        for (FileEntity file: files) {
            Long id;
            if (isMentor) {
                id  = file.getMentee().getId();
            } else {
                id  = file.getMentor().getId();
            }
            List<String> materialList = new ArrayList<>();
            List<String> text = new ArrayList<>();
            if (!uniques.containsKey(id) && isMentor) {
                materialList.add(file.getId());
                text.add(file.getText());
                TextResponse response = TextResponse.builder()
                        .id(id)
                        .firstName(file.getMentee().getUser().getFirstname())
                        .lastName(file.getMentee().getUser().getLastname())
                        .text(text)
                        .material(materialList)
                        .build();
                uniques.put(id, response);
            } else if (!uniques.containsKey(id) && !isMentor) {
                materialList.add(file.getId());
                text.add(file.getText());
                TextResponse response = TextResponse.builder()
                        .id(id)
                        .firstName(file.getMentor().getUser().getFirstname())
                        .lastName(file.getMentor().getUser().getLastname())
                        .material(materialList)
                        .text(text)
                        .build();
                uniques.put(id, response);
            } else {
                TextResponse tmp = uniques.get(id);
                List<String> materialId = tmp.getMaterial();
                List<String> newText = tmp.getText();
                materialId.add(file.getId());
                newText.add(file.getText());
                tmp.setMaterial(materialId);
                tmp.setText(newText);
                uniques.replace(id, tmp);
            }
        }
    }
}
