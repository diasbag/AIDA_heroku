package com.hackathon.mentor.service;

import com.hackathon.mentor.models.Skills;

import java.util.List;

public interface SkillsService {
    List<Skills> getListOfSKills(String partOfSkill);

    void postListOfSKills(String[] skill);

    List<Skills> getAllSkills();
}
