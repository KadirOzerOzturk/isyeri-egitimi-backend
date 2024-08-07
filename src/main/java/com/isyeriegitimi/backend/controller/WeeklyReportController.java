package com.isyeriegitimi.backend.controller;

import com.isyeriegitimi.backend.dto.WeeklyReportDto;
import com.isyeriegitimi.backend.model.WeeklyReport;
import com.isyeriegitimi.backend.service.WeeklyReportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/report")
public class WeeklyReportController {


    private WeeklyReportService weeklyReportService;

    public WeeklyReportController(WeeklyReportService weeklyReportService) {
        this.weeklyReportService = weeklyReportService;
    }


    @PostMapping("/saveReport")
    public ResponseEntity<?> saveReport(@RequestBody WeeklyReportDto weeklyReportDto){
        try {
            weeklyReportService.save(weeklyReportDto);
            return ResponseEntity.ok("Weekly report saved succesfully");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving weekly report ");


        }
    }
    @GetMapping("/{studentNo}")
    public ResponseEntity<List<WeeklyReport>> getReports(@PathVariable Long studentNo){
        return ResponseEntity.ok(weeklyReportService.getAllReportsByStudentNo(studentNo));
    }
    @PutMapping("/update/{reportId}")
    public ResponseEntity<String> updateReport(@PathVariable Long reportId,@RequestBody WeeklyReportDto weeklyReportDto){
        Optional<WeeklyReport> existingReport=weeklyReportService.getReportById(reportId);
        if (existingReport.isPresent())
        {
            WeeklyReport report= new WeeklyReport();
            report.setRaporId(reportId);
            report.setOgrenci(weeklyReportDto.getOgrenci());
            report.setTarih(existingReport.get().getTarih());
            report.setRaporIcerigi(weeklyReportDto.getRaporIcerigi());
            weeklyReportService.update(report);
            return ResponseEntity.ok("Succesfully updated");
        }else {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("delete/{studentNo}/{reportId}")
    public ResponseEntity<?> deleteWeeklyReport(@PathVariable Long studentNo, @PathVariable Long reportId) {
        try {
            weeklyReportService.deleteWeeklyReport(studentNo, reportId);
            return ResponseEntity.ok("Report removed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error removing report");
        }
    }
}
