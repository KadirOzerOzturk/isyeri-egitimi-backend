package com.isyeriegitimi.backend.repository;

import com.isyeriegitimi.backend.model.Application;
import com.isyeriegitimi.backend.model.Company;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ApplicationRepository extends JpaRepository<Application, UUID> {

    List<Application> findAllByAnnouncement_AnnouncementId(UUID id);
    List<Application> findByPendingRole(String role);
    List<Application> findAllByStudent_StudentId(UUID studentId);

    Optional<Application> findByApplicationId(UUID applicationId);

    @Transactional
    List<Application> findAllByStudent_StudentIdAndAnnouncement_CompanyCompanyId(UUID studentId,UUID companyId);


    boolean existsByStudentStudentIdAndAnnouncement_AnnouncementId(UUID studentStudentId, UUID announcementAnnouncementId);

    List<Application> findAllByAcceptingCompanyCompanyId(UUID companyId);
}
