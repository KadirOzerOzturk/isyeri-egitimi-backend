package com.isyeriegitimi.backend.controller.Announcement;

import com.isyeriegitimi.backend.dto.FavoriteAnnouncementDto;
import com.isyeriegitimi.backend.model.FavoriteAnnouncement;
import com.isyeriegitimi.backend.service.FavoriteAnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorites")
public class FavoriteAnnouncementController {

    private FavoriteAnnouncementService favoriteAnnouncementService;

    @Autowired
    public FavoriteAnnouncementController(FavoriteAnnouncementService favoriteAnnouncementService) {
        this.favoriteAnnouncementService = favoriteAnnouncementService;
    }

    @GetMapping("/{studentNo}")
    public List<FavoriteAnnouncement> getFavoriteAnnouncements(@PathVariable Long studentNo) {
        return favoriteAnnouncementService.getFavoriteAnnouncements(studentNo);
    }
    @PostMapping("/setFavorite")
    public ResponseEntity<?> setFavorite(@RequestBody FavoriteAnnouncementDto favoriteAnnouncementDto){
        try {
            favoriteAnnouncementService.save(favoriteAnnouncementDto);
            return ResponseEntity.ok("Favorite saved successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving favorite ");
        }

    }
    @DeleteMapping("/{studentNo}/{announcementId}")
    public ResponseEntity<?> deleteFavorite(@PathVariable Long studentNo, @PathVariable Long announcementId) {
        try {
            favoriteAnnouncementService.deleteFavoriteAnnouncement(studentNo, announcementId);
            return ResponseEntity.ok("Favorite removed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error removing favorite");
        }
    }



}
