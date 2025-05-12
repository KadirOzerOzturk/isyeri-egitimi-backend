package com.isyeriegitimi.backend.repository;

import com.isyeriegitimi.backend.model.FavoriteAnnouncement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FavoriteAnnouncementRepository extends JpaRepository<FavoriteAnnouncement, UUID> {

    List<FavoriteAnnouncement> findByStudent_StudentId(UUID studentId);
    Optional<FavoriteAnnouncement> findByStudent_StudentIdAndAnnouncement_AnnouncementId(UUID studentId, UUID announcementId);
    Optional<FavoriteAnnouncement> findByAnnouncement_AnnouncementId(UUID announcementId);
    void deleteAllByAnnouncement_AnnouncementId(UUID announcementId);
    void deleteByStudent_StudentIdAndAnnouncement_AnnouncementId(UUID studentId, UUID announcementId);
}
