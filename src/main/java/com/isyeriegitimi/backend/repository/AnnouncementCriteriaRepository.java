package com.isyeriegitimi.backend.repository;

import com.isyeriegitimi.backend.model.AnnouncementCriteria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnnouncementCriteriaRepository extends JpaRepository<AnnouncementCriteria ,Long > {

    List<AnnouncementCriteria> getAnnouncementCriteriaByIlanIlanId(Long id);
    void  deleteAllByIlan_IlanId(Long announcementId);
    void deleteByKriterId(Long criteriaId);
}
