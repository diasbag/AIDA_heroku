package com.hackathon.mentor.controllers;

import com.hackathon.mentor.models.Image;
import com.hackathon.mentor.models.Mentor;
import com.hackathon.mentor.models.Post;
import com.hackathon.mentor.models.User;
import com.hackathon.mentor.payload.request.PostRequest;
import com.hackathon.mentor.payload.response.PostResponse;
import com.hackathon.mentor.repository.MentorRepository;
import com.hackathon.mentor.repository.PostRepository;
import com.hackathon.mentor.repository.UserRepository;
import com.hackathon.mentor.security.services.UserDetailsImpl;
import com.hackathon.mentor.utils.FileNameHelper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
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

    private FileNameHelper fileHelper = new FileNameHelper();

    private String uploadFolder = "/resources";


    @GetMapping("/posts")
    public ResponseEntity<?> getPosts() {
        List<Post> posts = postRepository.getAll();
        List<PostResponse> postResponseList = new ArrayList<>();
        for (int i = 0; i < posts.size(); i++) {
            PostResponse postResponse = new PostResponse();
            postResponse.setTitle(posts.get(i).getTitle());
            postResponse.setArticle(posts.get(i).getArticle());
            postResponse.setDate(posts.get(i).getDate());
            postResponse.setImage(posts.get(i).getImage());
            postResponse.setUser(posts.get(i).getMentor().getUser());
            postResponseList.add(postResponse);
        }
        return new ResponseEntity<>(postResponseList, HttpStatus.OK);
    }

    @PostMapping(value = "/post/create", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE} )
    public ResponseEntity<?> createPost(@Valid @RequestPart(value = "post") PostRequest postRequest,
                                        @RequestPart(required = true) MultipartFile file) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();

        User user = userRepository.findByEmail(email).orElse(null);

        Mentor mentor = mentorRepository.findByUser(user);
        Image image = Image.buildImage(file, fileHelper);
        Post post = new Post();
        post.setDate(Date.from(Instant.now()));
        post.setTitle(postRequest.getTitle());
        post.setArticle(postRequest.getArticle());
        post.setMentor(mentor);
        post.setImage(image);
        postRepository.save(post);
        return new ResponseEntity<>("Success!!!", HttpStatus.OK);
    }

//    @PostMapping("/post/uploadImage")
//    public ResponseEntity<?> uploadPostImage() {
//        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String email = userDetails.getUsername();
//        User user = userRepository.findByEmail(email).orElse(null);
//
//        Mentor mentor = mentorRepository.findByUser(user);
//        Post post = postRepository.findByMentor(mentor);
//        return new ResponseEntity<>();
//    }
}
