package com.isyeriegitimi.backend.repository;

import com.isyeriegitimi.backend.model.WeeklyReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeeklyReportRepository extends JpaRepository<WeeklyReport ,Long> {
    List<WeeklyReport> findByOgrenci_OgrenciNo(Long ogrenciNo);
    WeeklyReport findByOgrenci_OgrenciNoAndAndRaporId(Long studentNo,Long reportId);

}
