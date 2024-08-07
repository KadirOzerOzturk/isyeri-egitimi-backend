package com.isyeriegitimi.backend.repository;

import com.isyeriegitimi.backend.model.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnnouncementRepository extends JpaRepository<Announcement,Long> {

    List<Announcement> getAnnouncementByFirmaFirmaId(Long firmaId);
    int countByIlanId(Long ilanId);
}
