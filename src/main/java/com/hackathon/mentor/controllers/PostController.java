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
import com.hackathon.mentor.service.PostService;
import com.hackathon.mentor.utils.FileNameHelper;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/posts")
    public ResponseEntity<?> getPosts() {
        List<PostResponse> postResponseList = postService.getPosts();
        return ResponseEntity.ok(postResponseList);
    }

    @PostMapping("/post/upload_text_2")
    public ResponseEntity<?> createPost (@RequestBody PostRequest postRequest) {
        Post post = postService.createPost(postRequest);
        return new ResponseEntity<>(post.getId(), HttpStatus.OK);
    }

    @PostMapping("/post/upload_image_1")
    public ResponseEntity<?> uploadPostImage(@RequestParam("file") MultipartFile file) {
        Post post = postService.uploadPostImage(file);
        return new ResponseEntity<>(post.getId(), HttpStatus.OK);
    }

    @GetMapping("/mentor/posts/{id}")
    public ResponseEntity<?> getMentorPostsById(@PathVariable("id") Long id) {
        Post post = postService.getByID(id);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

//    @GetMapping("/user/posts")
//    public ResponseEntity<?> getMentorPosts() {
//        List<Post> posts = postService.getAllByMentor();
//        return new ResponseEntity<>(posts, HttpStatus.OK);
//    }
    @PutMapping("/post/edit_post_text")
    public ResponseEntity<?> editPostText(@RequestBody PostRequest postRequest) {
        Post post = postService.editPostText(postRequest);
        return new ResponseEntity<>(post.getId(), HttpStatus.OK);
    }
    @PutMapping("/post/edit_post_image/{id}")
    public ResponseEntity<?> editPostImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        Post post = postService.editPostImage(id, file);
        return new ResponseEntity<>(post.getId(), HttpStatus.OK);
    }
    @DeleteMapping("/post/delete_post/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return new ResponseEntity<>("post with id " + id + " was deleted", HttpStatus.OK);
    }
}
