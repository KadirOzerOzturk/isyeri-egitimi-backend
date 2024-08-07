package com.isyeriegitimi.backend.controller.Announcement;


import com.isyeriegitimi.backend.model.AnnouncementCriteria;
import com.isyeriegitimi.backend.service.AnnouncementCriteriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/announcementCriteria")
public class AnnouncementCriteriaController {

    private AnnouncementCriteriaService announcementCriteriaService;

    @Autowired
    public AnnouncementCriteriaController(AnnouncementCriteriaService announcementCriteriaService) {
        this.announcementCriteriaService = announcementCriteriaService;
    }

    @PostMapping("/save/{announcementId}")
    public ResponseEntity<String> saveCriteria(@RequestBody List<AnnouncementCriteria>  announcementCriteria,@PathVariable Long announcementId){
        try {
            announcementCriteriaService.save(announcementCriteria,announcementId);
            return ResponseEntity.ok().body("Successfully saved");
        }catch (Exception e){
            return ResponseEntity.internalServerError().body("Criteria could not saved");
        }
    }

    @GetMapping("/{announcementId}")
    public ResponseEntity<?> getCriteriaByAnnouncementId(@PathVariable Long announcementId){
        try {

            return ResponseEntity.ok().body(announcementCriteriaService.getCriteriaByAnnouncementId(announcementId));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body("Criteria could not saved");
        }
    }
    @DeleteMapping("/delete/{criteriaId}")
    public ResponseEntity<String> deleteCriteria(@PathVariable Long criteriaId){
        try {
            return ResponseEntity.ok(announcementCriteriaService.deleteCriterias(criteriaId));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
