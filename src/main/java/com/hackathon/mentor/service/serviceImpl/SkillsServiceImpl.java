package com.hackathon.mentor.service.serviceImpl;

import com.hackathon.mentor.exceptions.AccountNotFound;
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
        log.info("skills were retrieved <<<");
        return skills;
    }

    @Override
    public void postListOfSKills(String[] skills) {
        log.info("saving new array of skills ...");
        for (String skill: skills) {
            Skills skillDB = new Skills(skill);
            skillsRepository.save(skillDB);
        }
        log.info("array of skills was saved <<<");
    }

    @Override
    public List<Skills> getAllSkills() {
        log.info("retrieving all skills ...");
        List<Skills> allSkills = skillsRepository.findAll();
        log.info("all skills were retrieved <<<");
        return allSkills;
    }

    @Override
    public void editSkill(String oldSkill, String newSkill) {
        log.info("editing skill ...");
        Skills skill = skillsRepository.findBySkill(oldSkill). orElseThrow(() -> new AccountNotFound(
                "skill - " + oldSkill));
        skill.setSkill(newSkill);
        skillsRepository.save(skill);
        log.info("skill was edited - " + oldSkill + " to " + newSkill + " <<<");
    }

    @Override
    public void deleteSkill(String skill) {
        log.info("started deleting of a skill ..");
        Skills skillForErase = skillsRepository.findBySkill(skill).orElseThrow(() -> new AccountNotFound(
                "skill - " + skill));
        skillsRepository.delete(skillForErase);
        log.info("skill was deleted <<<");
    }

    @Override
    public void postOneSKills(String skill) {
        log.info("saving new skill ...");
        Skills skillDB = new Skills(skill);
        skillsRepository.save(skillDB);
        log.info("new skill was saved <<<");
    }

}
