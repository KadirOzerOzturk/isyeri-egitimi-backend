package com.isyeriegitimi.backend.repository;

import com.isyeriegitimi.backend.model.AnnouncementCriteria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AnnouncementCriteriaRepository extends JpaRepository<AnnouncementCriteria , UUID> {



    List<AnnouncementCriteria> getAnnouncementCriteriaByAnnouncementAnnouncementId(UUID id);
    void  deleteAllByAnnouncement_AnnouncementId(UUID announcementId);
    void deleteAllByCriteriaId(UUID criteriaId);
}
