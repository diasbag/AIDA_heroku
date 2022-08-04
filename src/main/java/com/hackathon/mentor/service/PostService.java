package com.hackathon.mentor.service;

import com.hackathon.mentor.models.Post;
import com.hackathon.mentor.payload.request.PostRequest;
import com.hackathon.mentor.payload.response.PostResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    List<PostResponse> getPosts();

    Post createPost(PostRequest postRequest, String email);

    Post createPostWithImage(PostRequest postRequest, MultipartFile file);

    Post uploadPostImage(MultipartFile file);

    Post getByID(Long id);
//
//    List<Post> getAllByMentor();

    Post editPostText(PostRequest postRequest);
    Post editPostImage(Long id, MultipartFile file);

    void deletePost(Long id);
}
