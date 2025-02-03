package com.isyeriegitimi.backend.controller.Announcement;

import com.isyeriegitimi.backend.dto.FavoriteAnnouncementDto;
import com.isyeriegitimi.backend.model.ApiResponse;
import com.isyeriegitimi.backend.model.FavoriteAnnouncement;
import com.isyeriegitimi.backend.service.FavoriteAnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/favorites")
public class FavoriteAnnouncementController {

    private final FavoriteAnnouncementService favoriteAnnouncementService;

    @Autowired
    public FavoriteAnnouncementController(FavoriteAnnouncementService favoriteAnnouncementService) {
        this.favoriteAnnouncementService = favoriteAnnouncementService;
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<ApiResponse<List<FavoriteAnnouncement>>> getFavoriteAnnouncements(@PathVariable UUID studentId) {
        List<FavoriteAnnouncement> favorites = favoriteAnnouncementService.getFavoriteAnnouncements(studentId);
        return ResponseEntity.ok(ApiResponse.success(favorites, "Favorites retrieved successfully"));
    }

    @PostMapping("/setFavorite")
    public ResponseEntity<ApiResponse<UUID>> setFavorite(@RequestBody FavoriteAnnouncementDto favoriteAnnouncementDto) {
        try {

            return ResponseEntity.ok(ApiResponse.success(favoriteAnnouncementService.save(favoriteAnnouncementDto), "Favorite saved successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error saving favorite", HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    @DeleteMapping("/{studentId}/{announcementId}")
    public ResponseEntity<ApiResponse<String>> deleteFavorite(@PathVariable UUID studentId, @PathVariable UUID announcementId) {
        try {
            favoriteAnnouncementService.deleteFavoriteAnnouncement(studentId, announcementId);
            return ResponseEntity.ok(ApiResponse.success(null, "Favorite removed successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error removing favorite", HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }
}
