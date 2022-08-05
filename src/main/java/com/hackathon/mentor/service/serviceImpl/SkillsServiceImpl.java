package com.hackathon.mentor.service.serviceImpl;

import com.hackathon.mentor.models.Skills;
import com.hackathon.mentor.repository.SkillsRepository;
import com.hackathon.mentor.service.SkillsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SkillsServiceImpl implements SkillsService {
    private final SkillsRepository skillsRepository;
    @Override
    public List<Skills> getListOfSKills(String partOfSkill) {
        log.info("retrieving skill by part of the string ...");
        List<Skills> skills = skillsRepository.findAllBySkillContaining(partOfSkill);
        log.info("skills were retrieved");
        return skills;
    }

    @Override
    public void postListOfSKills(String[] skills) {
        log.info("saving skill ...");
        for (String skill: skills) {
            Skills skillDB = new Skills(skill);
            skillsRepository.save(skillDB);
        }
        log.info("skills were retrieved");
    }

    @Override
    public List<Skills> getAllSkills() {
        log.info("saving skill ...");
        List<Skill>
        log.info("skills were retrieved");
    }

}
