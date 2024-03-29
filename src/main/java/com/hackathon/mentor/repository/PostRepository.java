package com.hackathon.mentor.repository;

import com.hackathon.mentor.models.Mentor;
import com.hackathon.mentor.models.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value = "select * from posts", nativeQuery = true)
    Page<Post> getAll(Pageable pageable);

}
