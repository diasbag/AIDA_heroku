package com.hackathon.mentor.repository;

import com.hackathon.mentor.models.Skills;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SkillsRepository extends JpaRepository<Skills, Long> {

    List<Skills> findAllBySkillContaining(String partOfSkill);
    Optional<Skills> findBySkill(String skill);
}
