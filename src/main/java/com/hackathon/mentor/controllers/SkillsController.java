package com.hackathon.mentor.controllers;

import com.hackathon.mentor.models.Skills;
import com.hackathon.mentor.service.SkillsService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@CrossOrigin(value = "*",maxAge = 3600)
@RequiredArgsConstructor
@SecurityRequirement(name = "basicauth")
@RequestMapping("/skills")
public class SkillsController {
    private final SkillsService skillsService;
    @GetMapping("/get")
    public ResponseEntity<?> getListOfSkills(@RequestParam @NotBlank @NotNull String partOfSkill) {
        List<Skills> listOfSkills = skillsService.getListOfSKills(partOfSkill);
        return ResponseEntity.ok("{\"listOfSkills\":" + "\"" + listOfSkills + "\"}");
    }

    @PostMapping("/post")
    public ResponseEntity<?> postListOfSkills(@RequestParam @NotBlank @NotNull String[] skills) {
        skillsService.postListOfSKills(skills);
        return ResponseEntity.ok("skills were saved");
    }
    @PostMapping("/post_one")
    public ResponseEntity<?> postOneSkills(@RequestParam @NotBlank @NotNull String skill) {
        skillsService.postOneSKills(skill);
        return ResponseEntity.ok("skill was saved");
    }

    @GetMapping("/get_all")
    public ResponseEntity<?> getAllSkills() {
        List<Skills> listOfSkills = skillsService.getAllSkills();
        return ResponseEntity.ok("{\"listOfSkills\":" + "\"" + listOfSkills.toString() + "\"}");
    }

    @PutMapping("/edit")
    public ResponseEntity<?> editSkill(@RequestParam @NotBlank @NotNull String oldSkill,
                                       @NotBlank @NotNull String newSkill) {
        skillsService.editSkill(oldSkill, newSkill);
        return ResponseEntity.ok("skill was edited");
    }
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteSkill(@RequestParam @NotBlank @NotNull String skill) {
        skillsService.deleteSkill(skill);
        return ResponseEntity.ok("skill was deleted");
    }
}
