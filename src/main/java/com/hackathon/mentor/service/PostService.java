package com.hackathon.mentor.service;

import com.hackathon.mentor.models.Post;
import com.hackathon.mentor.payload.request.PostEditRequest;
import com.hackathon.mentor.payload.request.PostRequest;
import com.hackathon.mentor.payload.response.PostResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    List<PostResponse> getPosts();

    Post createPost(PostRequest postRequest, MultipartFile file);

//    Post uploadPostImage(MultipartFile file);

    Post getByID(Long id);

    List<Post> getAllByMentor();

    Post editPost(PostEditRequest postEditRequest, MultipartFile file);

    void deletePost(Long id);
}
