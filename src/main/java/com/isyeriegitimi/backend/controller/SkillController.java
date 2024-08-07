package com.isyeriegitimi.backend.controller;

import com.isyeriegitimi.backend.dto.SkillDto;
import com.isyeriegitimi.backend.model.Skill;
import com.isyeriegitimi.backend.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/skills")
public class SkillController {

    private SkillService skillService;

    @Autowired
    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveSkill(@RequestBody SkillDto skillDto){
        try {
            skillService.saveSkill(skillDto);
            return ResponseEntity.ok("Skill successfully saved");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving skill ");
        }
    }
    @GetMapping("/getSkills/{studentNo}")
    public ResponseEntity<List<Skill>> getSkill(@PathVariable Long studentNo){
        return ResponseEntity.ok(skillService.getSkills(studentNo));
    }
    @DeleteMapping("/delete/{studentNo}/{skillId}")
    public ResponseEntity<?> deleteSkill(@PathVariable Long studentNo,@PathVariable Long skillId ){
        try {
            skillService.deleteSkill(studentNo, skillId);
            return ResponseEntity.ok("Skill removed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error removing skill");
        }
    }
}
