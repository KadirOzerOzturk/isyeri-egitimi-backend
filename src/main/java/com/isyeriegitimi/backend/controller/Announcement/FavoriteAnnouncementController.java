package com.isyeriegitimi.backend.controller.Announcement;

import com.isyeriegitimi.backend.dto.FavoriteAnnouncementDto;
import com.isyeriegitimi.backend.dto.FavoriteAnnouncementRequest;
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
        return ResponseEntity.ok(ApiResponse.success(favorites, "Favorites fetched successfully"));
    }

    @PostMapping("/setFavorite")
    public ResponseEntity<ApiResponse<UUID>> setFavorite(@RequestBody FavoriteAnnouncementRequest favoriteAnnouncementRequest) {


            return ResponseEntity.ok(ApiResponse.success(favoriteAnnouncementService.save(favoriteAnnouncementRequest), "Favorite saved successfully"));

    }

    @DeleteMapping("/{favoriteId}")
    public ResponseEntity<ApiResponse<String>> deleteFavorite(@PathVariable UUID favoriteId) {

            favoriteAnnouncementService.delete(favoriteId);
            return ResponseEntity.ok(ApiResponse.success(null, "Favorite removed successfully"));

    }
}
