package com.isyeriegitimi.backend.repository;

import com.isyeriegitimi.backend.model.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AnnouncementRepository extends JpaRepository<Announcement, UUID> {


    List<Announcement> getAnnouncementByCompanyCompanyId(UUID companyId);
    int countByAnnouncementId(UUID ilanId);

    boolean existsByNo(String no);

    Optional<Announcement> findByNo(String no);
}

