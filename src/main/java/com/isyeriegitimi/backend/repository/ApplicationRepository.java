package com.isyeriegitimi.backend.repository;

import com.isyeriegitimi.backend.model.Application;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application,Long> {
    List<Application> findAllByIlanIlanId(Long id);
    List<Application> findAllByOgrenciOgrenciNo(Long studentNo);

    Optional<Application> findByBasvuruId(Long applicationId);

    @Transactional
    void deleteAllByOgrenciOgrenciNoAndAndIlanIlanId(Long studentNo,Long announcementId);
    @Transactional
    List<Application> findAllByOgrenciOgrenciNoAndFirmaFirmaId(Long studentNo,Long companyId);





}
