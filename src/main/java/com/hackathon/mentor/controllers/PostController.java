package com.hackathon.mentor.controllers;

import com.hackathon.mentor.models.Mentor;
import com.hackathon.mentor.models.Post;
import com.hackathon.mentor.models.User;
import com.hackathon.mentor.payload.request.PostRequest;
import com.hackathon.mentor.repository.MentorRepository;
import com.hackathon.mentor.repository.PostRepository;
import com.hackathon.mentor.repository.UserRepository;
import com.hackathon.mentor.security.services.UserDetailsImpl;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class PostController {

    @Autowired
    PostRepository postRepository;

    @Autowired
    MentorRepository mentorRepository;

    @Autowired
    UserRepository userRepository;


    private String uploadFolder = "/resources";


    @GetMapping("/posts")
    public ResponseEntity<?> getPosts() {
        List<Post> posts = postRepository.getAll();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @PostMapping("/post")
    public ResponseEntity<?> createPost(@Valid @RequestBody PostRequest postRequest, @RequestParam MultipartFile file, HttpServletRequest request) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();

        try{
            String uploadDirectory = request.getServletContext().getRealPath(uploadFolder);
            String fileName = file.getOriginalFilename();
            String filePath = Paths.get(uploadDirectory, fileName).toString();
            if (fileName == null || fileName.contains("..")) {
                return new ResponseEntity<>("Sorry! Filename contains invalid path sequence " + fileName, HttpStatus.BAD_REQUEST);
            }
           try {
               File dir = new File(uploadDirectory);
               if (!dir.exists()) {
                   dir.mkdir();
               }
               BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
               stream.write(file.getBytes());
               stream.close();
           } catch (Exception e) {
               e.printStackTrace();
           }
           byte[] imageData = file.getBytes();
            Post post = new Post();
            post.setTitle(postRequest.getTitle());
            post.setDate(Date.from(Instant.now()));
            post.setArticle(postRequest.getArticle());
            //post.setPhoto(imageData);
            User user = userRepository.getByEmail(email);
            Mentor mentor = mentorRepository.findByUser(user);
            post.setMentor(mentor);
            postRepository.save(post);

            return new ResponseEntity<>(post, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


    }

}
