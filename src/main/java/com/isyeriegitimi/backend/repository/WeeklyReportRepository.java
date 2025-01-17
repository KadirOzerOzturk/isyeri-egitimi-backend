package com.isyeriegitimi.backend.repository;

import com.isyeriegitimi.backend.model.WeeklyReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WeeklyReportRepository extends JpaRepository<WeeklyReport ,UUID> {
    List<WeeklyReport> findByStudent_StudentNumber(String studentNo);
    Optional<WeeklyReport> findByStudent_StudentNumberAndReportId(String studentNo, UUID reportId);


}
