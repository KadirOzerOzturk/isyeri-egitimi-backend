package com.isyeriegitimi.backend.service;

import com.isyeriegitimi.backend.dto.WeeklyReportDto;
import com.isyeriegitimi.backend.exceptions.InternalServerErrorException;
import com.isyeriegitimi.backend.exceptions.ResourceNotFoundException;
import com.isyeriegitimi.backend.model.Student;
import com.isyeriegitimi.backend.model.WeeklyReport;
import com.isyeriegitimi.backend.repository.StudentRepository;
import com.isyeriegitimi.backend.repository.WeeklyReportRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WeeklyReportService {

    private WeeklyReportRepository weeklyReportRepository;
    private StudentRepository studentRepository;

    public WeeklyReportService(WeeklyReportRepository weeklyReportRepository, StudentRepository studentRepository) {
        this.weeklyReportRepository = weeklyReportRepository;
        this.studentRepository = studentRepository;
    }

    public Optional<WeeklyReport> getReportById(UUID reportId){
        return weeklyReportRepository.findById(reportId);
    }

    public UUID save(WeeklyReportDto weeklyReportDto){
        try{
            Student student=studentRepository.findByStudentNumber(weeklyReportDto.getStudent().getStudentNumber())
                    .orElseThrow(()-> new ResourceNotFoundException("Student","studentNumber",weeklyReportDto.getStudent().getStudentNumber()));

            WeeklyReport weeklyReport=WeeklyReport
                    .builder()
                    .student(student)
                    .report(weeklyReportDto.getReport())
                    .reportDate(weeklyReportDto.getReportDate())
                    .build();
            weeklyReportRepository.save(weeklyReport);
            System.out.println("weekly report id : "+weeklyReport.getReportId());
            return weeklyReport.getReportId();
        }
        catch (Exception e){
            throw new InternalServerErrorException("An error occurred while saving the report: "+e.getMessage());
        }
    }


    public List<WeeklyReport> getAllReportsByStudentNo(String studentNo){
        try {
            List<WeeklyReport>  weeklyReports=weeklyReportRepository.findByStudent_StudentNumber(studentNo);
            if (weeklyReports.isEmpty()){
                throw new ResourceNotFoundException("Weekly Report","studentNo",studentNo);
            }
            Collections.sort(weeklyReports, new Comparator<WeeklyReport>() {
                @Override
                public int compare(WeeklyReport w1, WeeklyReport w2) {
                    return w2.getReportDate().compareTo(w1.getReportDate());
                }
            });
            return weeklyReports;
        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred while fetching the reports: "+e.getMessage());
        }
    }

   public void update(UUID reportId, WeeklyReportDto weeklyReportDto) {
    try {
        Optional<WeeklyReport> existingReport = weeklyReportRepository.findById(reportId);
        if (existingReport.isPresent()) {
            WeeklyReport report = existingReport.get();
            report.setStudent(studentRepository.findByStudentNumber(weeklyReportDto.getStudent().getStudentNumber())
                    .orElseThrow(() -> new ResourceNotFoundException("Student", "studentNumber", weeklyReportDto.getStudent().getStudentNumber())));
            report.setReportDate(weeklyReportDto.getReportDate());
            report.setReport(weeklyReportDto.getReport());
            weeklyReportRepository.save(report);
        } else {
            throw new ResourceNotFoundException("Weekly Report", "reportId", reportId.toString());
        }
    } catch (Exception e) {
        throw new InternalServerErrorException("An error occurred while updating the report: " + e.getMessage());
    }
}

    public void deleteWeeklyReport(String studentNo, UUID reportId) {
        try {
           WeeklyReport weeklyReport = weeklyReportRepository.findByStudent_StudentNumberAndReportId(studentNo, reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Weekly Report", "studentNo", studentNo));
           weeklyReportRepository.delete(weeklyReport);
        }catch (Exception e){
            throw new InternalServerErrorException("An error occurred while deleting the report: "+e.getMessage());
        }
    }
}
