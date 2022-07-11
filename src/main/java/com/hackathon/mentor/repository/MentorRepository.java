package com.hackathon.mentor.repository;

import com.hackathon.mentor.models.Mentor;
import com.hackathon.mentor.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MentorRepository extends JpaRepository<Mentor, Long> {

    Optional<Mentor> findById(Long id);

    @Query(value = "SELECT * FROM mentors", nativeQuery = true)
    List<Mentor> getAll();

    Mentor findByUser(User user);
    Page<Mentor> findAll(Pageable pageable);
}
