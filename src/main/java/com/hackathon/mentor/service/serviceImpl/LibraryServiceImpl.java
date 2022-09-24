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

    @Override
    public String save(MultipartFile file) throws IOException {
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
        if (mentorRepository.findByUser(user).isPresent()) {
            Mentor mentor = mentorRepository.getByUser(user);
            fileEntity.setMentor(mentor);
        } else if (menteeRepository.findByUser(user).isPresent()) {
            Mentee mentee = menteeRepository.findByUser(user).get();
            fileEntity.setMentee(mentee);
        } else {
            throw new AccountNotFound("mentor or mentee with id - " + email);
        }
        FileEntity fileSaved = fileRepository.save(fileEntity);
        log.info("file was saved <<<");
        return fileSaved.getId();
    }

    @Override
    public FileEntity getFile(String id) {
        log.info("finding file  - " + id + " ...");
        FileEntity out = fileRepository.findById(id).orElseThrow(() -> {throw new AccountNotFound(
                "file with id - " + id);
        });
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

    @Override
    public void postText(LibraryTextRequest libraryTextRequest) {
        String email = Utils.getEmail();
        User user =  userRepository.findByEmail(email).orElseThrow(() -> {
                    throw new AccountNotFound(
                            "user with email" + email);
                });
        Long id = libraryTextRequest.getId();
        List<FileEntity> files;
        if (mentorRepository.findByUser(user).isPresent()) {
            Long mentorId = mentorRepository.findByUser(user).get().getId();
            files = fileRepository.findByMentor_Id(mentorId);
            for (FileEntity file: files) {
                if (file.getMentee().getId().equals(id)) {
                    file.setText(libraryTextRequest.getText());
                }
            }
        } else if (mentorRepository.findByUser(user).isPresent()) {
            Long menteeId = mentorRepository.findByUser(user).get().getId();
            files = fileRepository.findByMentee_Id(menteeId);
            for (FileEntity file: files) {
                if (file.getMentor().getId().equals(id)) {
                    file.setText(libraryTextRequest.getText());
                }
            }
        } else {
            throw new AccountNotFound(" file by user details - " + email);
        }
    }

    @Override
    public List<TextResponse> getText() {
        String email = Utils.getEmail();
        User user = userRepository.findByEmail(email).orElseThrow(() -> {throw new AccountNotFound(
                "user with email - " + email);
        });
        HashMap<Long, TextResponse> uniques = new HashMap<>();
        if (mentorRepository.findByUser(user).isPresent()) {
            Mentor mentor = mentorRepository.getByUser(user);
            List<FileEntity> files = fileRepository.findByMentor_Id(mentor.getId());
            textResponseListBuilder(uniques, files);
        } else if (menteeRepository.findByUser(user).isPresent()) {
            Mentee mentee = menteeRepository.findByUser(user).get();
            List<FileEntity> files = fileRepository.findByMentee_Id(mentee.getId());
            textResponseListBuilder(uniques, files);
        } else {
            throw new AccountNotFound("user with email - " + email);
        }
        return new ArrayList<>(uniques.values());
    }

    private void textResponseListBuilder(HashMap<Long, TextResponse> uniques, List<FileEntity> files) {
        for (FileEntity file: files) {
            Long id  = file.getMentee().getId();
            if (!uniques.containsKey(id)) {
                List<String> materialList = new ArrayList<>();
                materialList.add(file.getId());
                TextResponse response = TextResponse.builder()
                        .id(id)
                        .firstName(file.getMentee().getUser().getFirstname())
                        .lastName(file.getMentee().getUser().getLastname())
                        .material(materialList)
                        .build();
                uniques.put(id, response);
            } else {
                TextResponse tmp = uniques.get(id);
                List<String> materialId = tmp.getMaterial();
                materialId.add(file.getId());
                tmp.setMaterial(materialId);
                uniques.replace(id, tmp);
            }
        }
    }
}
