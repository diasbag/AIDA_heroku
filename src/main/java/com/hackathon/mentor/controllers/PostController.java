package com.hackathon.mentor.controllers;

import com.hackathon.mentor.models.Post;
import com.hackathon.mentor.payload.request.PostRequest;
import com.hackathon.mentor.payload.response.PostResponse;
import com.hackathon.mentor.service.PostService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@SecurityRequirement(name = "basicauth")
public class PostController {
    private final PostService postService;

    @GetMapping("/posts")
    public ResponseEntity<?> getPosts(@RequestParam("page") Integer page, @RequestParam("lang") String lang) {
        return postService.getPosts(page, lang);
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
    @PostMapping("/post/upload_post_image")
    public ResponseEntity<?> uploadPostImageText(@RequestBody PostRequest postRequest,
                                                 @RequestParam("file") MultipartFile file) {
        Post post = postService.createPostWithImage(postRequest, file);
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
