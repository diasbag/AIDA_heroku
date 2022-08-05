package com.hackathon.mentor.repository;

import com.hackathon.mentor.models.Skills;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillsRepository extends JpaRepository<Skills, Long> {

    List<Skills> findAllBySkillContaining(String partOfSkill);
}
