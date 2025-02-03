package com.isyeriegitimi.backend.repository;

import com.isyeriegitimi.backend.model.Application;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ApplicationRepository extends JpaRepository<Application, UUID> {

    List<Application> findAllByAnnouncement_AnnouncementId(UUID id);
    List<Application> findAllByStudent_StudentNumber(String studentNo);
    List<Application> findAllByStudent_StudentId(UUID studentId);

    Optional<Application> findByApplicationId(UUID applicationId);

    @Transactional
    void deleteAllByStudentStudentNumberAndAnnouncementAnnouncementId(String studentNo,UUID announcementId);
    @Transactional
    List<Application> findAllByStudent_StudentNumberAndCompanyCompanyId(String studentNo,UUID companyId);
    @Transactional
    List<Application> findAllByStudent_StudentIdAndCompanyCompanyId(UUID studentId,UUID companyId);


}
