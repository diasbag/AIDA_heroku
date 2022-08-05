package com.hackathon.mentor.controllers;

import com.hackathon.mentor.models.Skills;
import com.hackathon.mentor.service.MentorService;
import com.hackathon.mentor.service.SkillsService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(value = "*",maxAge = 3600)
@RequiredArgsConstructor
@SecurityRequirement(name = "basicauth")
@RequestMapping("/skills")
public class SkillsController {
    private final SkillsService skillsService;
    @GetMapping("/get")
    public ResponseEntity<?> getListOfSkills(@RequestParam String partOfSkill) {
        List<Skills> listOfSkills = skillsService.getListOfSKills(partOfSkill);
        return ResponseEntity.ok("{\"listOfSkills\":" + "\"" + listOfSkills + "\"}");
    }

    @PostMapping("/post")
    public ResponseEntity<?> postListOfSkills(@RequestParam String[] skills) {
        skillsService.postListOfSKills(skills);
        return ResponseEntity.ok("skill was saved");
    }

    @GetMapping("/get_all")
    public ResponseEntity<?> getAllSkills() {
        List<Skills> listOfSkills = skillsService.getAllSkills();
        return ResponseEntity.ok("{\"listOfSkills\":" + "\"" + listOfSkills.toString() + "\"}");
    }

}
