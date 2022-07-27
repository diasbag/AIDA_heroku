package com.hackathon.mentor.service.serviceImpl;

import com.hackathon.mentor.exceptions.AccountNotFound;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
@Slf4j
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final MentorRepository mentorRepository;
    private final FileNameHelper fileHelper = new FileNameHelper();
    @Override
    public List<PostResponse> getPosts() {
        log.info("getting all posts ...");
        List<Post> posts = postRepository.getAll();
        List<PostResponse> postResponseList = new ArrayList<>();
        for (Post post : posts) {
            PostResponse postResponse = new PostResponse();
            postResponse.setTitle(post.getTitle());
            postResponse.setArticle(post.getArticle());
            postResponse.setDate(post.getDate());
            postResponse.setImage(post.getImage());
            postResponseList.add(postResponse);
        }
        log.info("all posts were found <<<");
        return postResponseList;
    }

    @Override
    public Post createPost(PostRequest postRequest, MultipartFile file) {
        log.info("creating post ...");
        Post post = new Post();
        post.setTitle(postRequest.getTitle());
        post.setArticle(postRequest.getArticle());
        post.setDate(Date.from(Instant.now()));
        Image image = Image.buildImage(file, fileHelper);
        post.setImage(image);
        postRepository.save(post);
        log.info("post was created <<<");
        return post;
    }
//
//    @Override
//    public Post uploadPostImage(MultipartFile file) {
//        log.info("uploading post image ...");
//        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String email = userDetails.getUsername();
//        Image image = Image.buildImage(file, fileHelper);
//        Post post = new Post();
//        post.setImage(image);
//        postRepository.save(post);
//        log.info("photo was uploaded <<<");
//        return post;
//    }

    @Override
    public Post getByID(Long id) {
        log.info("getting post by mentor id ...");
        Post post = postRepository.findById(id).orElseThrow(() -> new AccountNotFound(" user with id " + id));
        log.info("all posts by mentor id were retrieved <<<");
        return post;
    }

    @Override
    public List<Post> getAllByMentor() {
        log.info("getting all mentor posts ...");
        List<Post> posts = postRepository.findAll();
        log.info("all mentor posts were retrieved <<<");
        return posts;
    }

    @Override
    public Post editPost(PostEditRequest postEditRequest, MultipartFile file) {
        log.info("editing post ...");
        Long id = postEditRequest.getId();
        Post post = postRepository.findById(id).orElseThrow(() -> new AccountNotFound("post with id " + id));
        post.setArticle(postEditRequest.getArticle());
        post.setTitle(postEditRequest.getTitle());
        post.setDate(Date.from(Instant.now()));
        Image image = Image.buildImage(file, fileHelper);
        post.setImage(image);
        postRepository.save(post);
        log.info("post was edited " + post + " <<<");
        return post;
    }

    @Override
    public void deletePost(Long id) {
        log.info("deleting post ...");
        Post post = postRepository.findById(id).orElseThrow(() -> new AccountNotFound("post with id" + id));
        postRepository.delete(post);
        log.info("post was deleted: " + post + " <<<");
    }
}
