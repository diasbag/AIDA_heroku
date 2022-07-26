package com.hackathon.mentor.controllers;

import com.hackathon.mentor.models.Image;
import com.hackathon.mentor.models.Mentor;
import com.hackathon.mentor.models.Post;
import com.hackathon.mentor.models.User;
import com.hackathon.mentor.payload.request.PostEditRequest;
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

    @PostMapping("/post/create")
    public ResponseEntity<?> createPost (@RequestBody PostRequest postRequest,
                                         @RequestParam("file") MultipartFile file) {
        Post post = postService.createPost(postRequest, file);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

//    @PostMapping("/post/uploadImage")
//    public ResponseEntity<?> uploadPostImage(@RequestParam("file") MultipartFile file) {
//        Post post = postService.uploadPostImage(file);
//        return new ResponseEntity<>(post.getId(), HttpStatus.OK);
//    }

    @GetMapping("/mentor/{id}/posts")
    public ResponseEntity<?> getMentorPostsById(@PathVariable("id") Long id) {
        List<Post> posts = postService.getByID(id);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/user/posts")
    public ResponseEntity<?> getMentorPosts() {
        List<Post> posts = postService.getAllByMentor();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }
    @PostMapping("/post/edit_post")
    public ResponseEntity<?> editPost(@RequestBody PostEditRequest postEditRequest,
                                      @RequestParam("file") MultipartFile file) {
        Post post = postService.editPost(postEditRequest, file);
        return new ResponseEntity<>(post.getId(), HttpStatus.OK);
    }
    @DeleteMapping("/post/delete_post")
    public ResponseEntity<?> deletePost(@RequestParam Long id) {
        postService.deletePost(id);
        return new ResponseEntity<>("post with was deleted", HttpStatus.OK);
    }
}
