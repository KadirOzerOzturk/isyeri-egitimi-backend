package com.isyeriegitimi.backend.controller;

import com.isyeriegitimi.backend.dto.SkillDto;
import com.isyeriegitimi.backend.model.ApiResponse;
import com.isyeriegitimi.backend.model.Skill;
import com.isyeriegitimi.backend.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/skills")
public class SkillController {

    private SkillService skillService;

    @Autowired
    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping("/save")
    public ResponseEntity<ApiResponse<?>> saveSkill(@RequestBody SkillDto skillDto){

            skillService.saveSkill(skillDto);
            return ResponseEntity.ok(ApiResponse.success(null,"Skill saved successfully"));
    }
    @GetMapping("/getSkills/{studentNo}")
    public ResponseEntity<ApiResponse<List<Skill>>> getSkill(@PathVariable String studentNo){
        return ResponseEntity.ok(ApiResponse.success(skillService.getSkills(studentNo),"Skills fetched successfully"));
    }
    @DeleteMapping("/delete/{studentNo}/{skillId}")
    public ResponseEntity<ApiResponse<?>> deleteSkill(@PathVariable String studentNo,@PathVariable UUID skillId ){
            skillService.deleteSkill(studentNo, skillId);
            return ResponseEntity.ok(ApiResponse.success(null, "Skill removed successfully"));

    }
}
