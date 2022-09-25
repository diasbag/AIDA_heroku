package com.hackathon.mentor.repository;

import com.hackathon.mentor.models.FileEntity;
import com.hackathon.mentor.models.Mentee;
import com.hackathon.mentor.models.Mentor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, String> {

    List<FileEntity> findByMentor_Id(Long id);

    List<FileEntity> findByMentee_Id(Long id);

    void deleteAllByMentorAndMentee(Mentor mentor, Mentee mentee);
}
