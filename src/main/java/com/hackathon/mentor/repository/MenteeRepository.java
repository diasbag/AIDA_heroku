package com.hackathon.mentor.repository;

import com.hackathon.mentor.models.Mentee;
import com.hackathon.mentor.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenteeRepository extends JpaRepository<Mentee, Long> {

    @Query(value = "SELECT * FROM mentees", nativeQuery = true)
    List<Mentee> getAll();

    Mentee findByUser(User user);
}
