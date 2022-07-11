package com.hackathon.mentor.repository;

import com.hackathon.mentor.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value = "select * from posts", nativeQuery = true)
    List<Post> getAll();
}
