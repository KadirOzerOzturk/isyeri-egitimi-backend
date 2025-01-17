package com.isyeriegitimi.backend.repository;

import com.isyeriegitimi.backend.model.FavoriteAnnouncement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FavoriteAnnouncementRepository extends JpaRepository<FavoriteAnnouncement, UUID> {

    List<FavoriteAnnouncement> findByStudent_StudentNumber(String studentNo);
    Optional<FavoriteAnnouncement> findByStudent_StudentNumberAndAnnouncement_AnnouncementId(String studentNo, UUID announcementId);
}
