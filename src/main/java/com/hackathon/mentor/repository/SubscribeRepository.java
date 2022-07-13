package com.hackathon.mentor.repository;

import com.hackathon.mentor.models.Mentee;
import com.hackathon.mentor.models.Mentor;
import com.hackathon.mentor.models.Subscribe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {

    @Query(value = "SELECT * from subscribes", nativeQuery = true)
    List<Subscribe> getAll();

    List<Subscribe> findByMentor(Mentor mentor);

    Subscribe getByMentorAndMentee(Mentor mentor, Mentee mentee);

    void deleteByMentorAndMentee(Mentor mentor, Mentee mentee);
}
