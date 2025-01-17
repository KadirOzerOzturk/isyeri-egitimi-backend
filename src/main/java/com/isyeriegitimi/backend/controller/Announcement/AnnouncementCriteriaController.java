package com.isyeriegitimi.backend.controller.Announcement;


import com.isyeriegitimi.backend.model.AnnouncementCriteria;
import com.isyeriegitimi.backend.model.ApiResponse;
import com.isyeriegitimi.backend.service.AnnouncementCriteriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@RestController
@RequestMapping("/announcementCriteria")
public class AnnouncementCriteriaController {

    private AnnouncementCriteriaService announcementCriteriaService;

    @Autowired
    public AnnouncementCriteriaController(AnnouncementCriteriaService announcementCriteriaService) {
        this.announcementCriteriaService = announcementCriteriaService;
    }

    @PostMapping("/save/{announcementId}")
    public ResponseEntity<ApiResponse<String>> saveCriteria(@RequestBody List<AnnouncementCriteria> announcementCriteria, @PathVariable UUID announcementId) {
        announcementCriteriaService.save(announcementCriteria, announcementId);
        return ResponseEntity.ok(ApiResponse.success(null, "Criteria saved successfully."));
    }

    @GetMapping("/{announcementId}")
    public ResponseEntity<ApiResponse<List<AnnouncementCriteria>>> getCriteriaByAnnouncementId(@PathVariable UUID announcementId) {

        return ResponseEntity.ok(ApiResponse.success( announcementCriteriaService.getCriteriaByAnnouncementId(announcementId), "Criteria fetched successfully."));
    }

    @DeleteMapping("/delete/{criteriaId}")
    public ResponseEntity<ApiResponse<String>> deleteCriteria(@PathVariable UUID criteriaId) {
        return ResponseEntity.ok(ApiResponse.success(null,"Criteria deleted successfully."));

    }
}
