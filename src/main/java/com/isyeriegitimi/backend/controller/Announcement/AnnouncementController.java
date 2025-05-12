package com.isyeriegitimi.backend.controller.Announcement;


import com.isyeriegitimi.backend.dto.AnnouncementDto;
import com.isyeriegitimi.backend.model.Announcement;
import com.isyeriegitimi.backend.model.ApiResponse;
import com.isyeriegitimi.backend.service.AnnouncementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@RestController
@RequestMapping("/announcements")
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @Autowired
    public AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Announcement>>> getAllAnnouncements() {

        return ResponseEntity.ok(ApiResponse.success(announcementService.getAllAnnouncements(), "Announcements fetched successfully."));
    }

    @GetMapping("/all/{companyId}")
    public ResponseEntity<ApiResponse<List<Announcement>>> getAnnouncementsByCompanyId(@PathVariable UUID companyId) {

        return ResponseEntity.ok(ApiResponse.success(announcementService.getAnnouncementsByCompanyId(companyId), "Announcements fetched successfully."));
    }

    @GetMapping("/{announcementId}")
    public ResponseEntity<ApiResponse<Announcement>> getAnnouncementsById(@PathVariable UUID announcementId) {

        return ResponseEntity.ok(ApiResponse.success(announcementService.getAnnouncementsById(announcementId), "Announcement fetched successfully."));
    }
    @GetMapping("/byNo/{no}")
    public ResponseEntity<ApiResponse<Announcement>> getAnnouncementsByNo(@PathVariable String no) {
        return ResponseEntity.ok(ApiResponse.success(announcementService.getAnnouncementsByNo(no),"Announcement fetched successfully"));
    }

    @PostMapping("/share")
    public ResponseEntity<ApiResponse<UUID>> shareAnnouncement(@RequestBody AnnouncementDto announcementDto) {
        return ResponseEntity.ok(ApiResponse.success(announcementService.save(announcementDto), "Announcement shared successfully."));
    }

    @PutMapping("/update/{announcementId}")
    public ResponseEntity<ApiResponse<Void>> updateAnnouncement(
            @PathVariable UUID announcementId, @RequestBody AnnouncementDto announcementDto) {
        announcementService.updateAnnouncement(announcementId, announcementDto);
        return ResponseEntity.ok(ApiResponse.success(null, "Announcement updated successfully."));
    }

    @DeleteMapping("/delete/{announcementId}")
    public ResponseEntity<ApiResponse<Void>> deleteAnnouncement(@PathVariable UUID announcementId) {
     announcementService.deleteAnnouncement(announcementId);
        return ResponseEntity.ok(ApiResponse.success(null, "Announcement deleted successfully."));
    }
}

