package com.hackathon.mentor.service;

import com.hackathon.mentor.models.Skills;

import java.util.List;

public interface SkillsService {
    List<Skills> getListOfSKills(String partOfSkill);

    void postListOfSKills(String[] skill);

    List<Skills> getAllSkills();

    void editSkill(String oldSkill, String newSkill);

    void deleteSkill(String skill);

    void postOneSKills(String skill);
}
