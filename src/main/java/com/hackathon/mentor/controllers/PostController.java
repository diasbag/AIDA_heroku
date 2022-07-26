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
import com.hackathon.mentor.utils.FileNameHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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


//    @PostMapping(value = "/post/create", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE} )
//    public ResponseEntity<?> createPost(@Valid @RequestPart(value = "post") PostRequest postRequest,
//                                        @RequestPart(name = "file") MultipartFile file) {
//        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String email = userDetails.getUsername();
//
//        User user = userRepository.findByEmail(email).orElse(null);
//
//        Mentor mentor = mentorRepository.findByUser(user);
//        Image image = Image.buildImage(file, fileHelper);
//        Post post = new Post();
//        post.setDate(Date.from(Instant.now()));
//        post.setTitle(postRequest.getTitle());
//        post.setArticle(postRequest.getArticle());
//        post.setMentor(mentor);
//        post.setImage(image);
//        postRepository.save(post);
//        return new ResponseEntity<>("Success!!!", HttpStatus.OK);
//    }

    @PostMapping("/post/{id}/create")
    public ResponseEntity<?> createPost (@PathVariable("id") Long id, @RequestBody PostRequest postRequest) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Not found!!!"));
        Mentor mentor = mentorRepository.findByUser(user);

        Post post = postRepository.findByIdAndMentor(id, mentor);

        post.setTitle(postRequest.getTitle());
        post.setArticle(postRequest.getArticle());
        post.setDate(Date.from(Instant.now()));

        postRepository.save(post);

        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @PostMapping("/post/uploadImage")
    public ResponseEntity<?> uploadPostImage(@RequestParam("file") MultipartFile file) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email).orElse(null);
        Mentor mentor = mentorRepository.findByUser(user);
        Image image = Image.buildImage(file, fileHelper);
        Post post = new Post();
        post.setMentor(mentor);
        post.setImage(image);
        postRepository.save(post);
        return new ResponseEntity<>(post.getId(), HttpStatus.OK);
    }

    @GetMapping("/mentor/{id}/posts")
    public ResponseEntity<?> getMentorPostsById(@PathVariable("id") Long id) {
        Mentor mentor = mentorRepository.findById(id).orElse(null);
        List<Post> posts = postRepository.getByMentor(mentor);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/user/posts")
    public ResponseEntity<?> getMentorPosts() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email).orElse(null);
        Mentor mentor = mentorRepository.findByUser(user);
        List<Post> posts = postRepository.getByMentor(mentor);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }
//    dcsdcsdc
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
