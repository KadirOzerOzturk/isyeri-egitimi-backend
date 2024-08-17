package com.isyeriegitimi.backend.controller.Announcement;


import com.isyeriegitimi.backend.dto.AnnouncementDto;
import com.isyeriegitimi.backend.model.Announcement;
import com.isyeriegitimi.backend.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/announcements")
public class AnnouncementController {

    private AnnouncementService announcementService;

    @Autowired
    public AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    @GetMapping
    public ResponseEntity<?> getAllAnnouncements(){
        return ResponseEntity.ok(announcementService.getAllAnnouncements());
    }
    @GetMapping("/all/{companyId}")
    public ResponseEntity<List<Announcement>> getAnnouncementsByCompanyId(@PathVariable Long companyId){
        return ResponseEntity.ok(announcementService.getAnnouncementsByCompanyId(companyId));
    }
    @GetMapping("/{announcementId}")
    public ResponseEntity<?> getAnnouncementsById(@PathVariable Long announcementId){
        Optional<Announcement> announcement=announcementService.getAnnouncementsById(announcementId);
        if (announcement.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(announcement);

    }

    @PostMapping("/share" )
    public ResponseEntity<?> shareAnnouncement(@RequestBody AnnouncementDto announcementDto){


                return ResponseEntity.ok(announcementService.save(announcementDto));

    }
    @PutMapping("/update/{announcementId}")
    public ResponseEntity<?> updateAnnouncement(@PathVariable Long announcementId,@RequestBody AnnouncementDto announcementDto){

        try {
            announcementService.updateAnnouncement(announcementId,announcementDto);
            return ResponseEntity.ok("Successfully updated");

        }catch (Exception e){
            return ResponseEntity.internalServerError().body("Delete failed");
        }

    }

    @DeleteMapping("/delete/{announcementId}")
    public ResponseEntity<?> deleteAnnouncement(@PathVariable Long announcementId){
        try {
            announcementService.deleteAnnouncement(announcementId);
            return ResponseEntity.ok("Successfully deleted");
        }catch (Exception e){
            return ResponseEntity.internalServerError().body("Delete failed");
        }
    }

}
