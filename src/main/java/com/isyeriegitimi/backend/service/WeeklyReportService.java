package com.isyeriegitimi.backend.service;

import com.isyeriegitimi.backend.dto.WeeklyReportDto;
import com.isyeriegitimi.backend.model.Student;
import com.isyeriegitimi.backend.model.WeeklyReport;
import com.isyeriegitimi.backend.repository.StudentRepository;
import com.isyeriegitimi.backend.repository.WeeklyReportRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class WeeklyReportService {

    private WeeklyReportRepository weeklyReportRepository;
    private StudentRepository studentRepository;

    public WeeklyReportService(WeeklyReportRepository weeklyReportRepository, StudentRepository studentRepository) {
        this.weeklyReportRepository = weeklyReportRepository;
        this.studentRepository = studentRepository;
    }

    public Optional<WeeklyReport> getReportById(Long reportId){
        return weeklyReportRepository.findById(reportId);
    }

    public void save(WeeklyReportDto weeklyReportDto){
        try{
            Student student=studentRepository.findByOgrenciNo(weeklyReportDto.getOgrenci().getOgrenciNo()).orElseThrow(()-> new RuntimeException("User Not Found"));

            WeeklyReport weeklyReport=WeeklyReport
                    .builder()
                    .ogrenci(student)
                    .raporIcerigi(weeklyReportDto.getRaporIcerigi())
                    .tarih(weeklyReportDto.getTarih())
                    .build();
            weeklyReportRepository.save(weeklyReport);
        }
        catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Error saving weekly reposrt", e);
        }
    }


    public List<WeeklyReport> getAllReportsByStudentNo(Long studentNo){
        try {
            List<WeeklyReport>  weeklyReports=weeklyReportRepository.findByOgrenci_OgrenciNo(studentNo);
            Collections.sort(weeklyReports, new Comparator<WeeklyReport>() {
                @Override
                public int compare(WeeklyReport w1, WeeklyReport w2) {
                    return w2.getTarih().compareTo(w1.getTarih());
                }
            });
            return weeklyReports;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void update(WeeklyReport report) {
        weeklyReportRepository.save(report);

    }

    public void deleteWeeklyReport(Long studentNo, Long reportId) {
        try {
            WeeklyReport weeklyReport=weeklyReportRepository.findByOgrenci_OgrenciNoAndAndRaporId(studentNo,reportId);
            weeklyReportRepository.delete(weeklyReport);

        }catch (Exception e){
            throw new RuntimeException("Error removing report", e);
        }
    }
}
