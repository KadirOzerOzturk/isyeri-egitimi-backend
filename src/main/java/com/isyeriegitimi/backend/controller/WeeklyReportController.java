package com.isyeriegitimi.backend.controller;

import com.isyeriegitimi.backend.dto.WeeklyReportDto;
import com.isyeriegitimi.backend.model.ApiResponse;
import com.isyeriegitimi.backend.model.WeeklyReport;
import com.isyeriegitimi.backend.service.WeeklyReportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/report")
public class WeeklyReportController {


    private WeeklyReportService weeklyReportService;

    public WeeklyReportController(WeeklyReportService weeklyReportService) {
        this.weeklyReportService = weeklyReportService;
    }


    @PostMapping("/saveReport")
    public ResponseEntity<ApiResponse<?>> saveReport(@RequestBody WeeklyReportDto weeklyReportDto){

            return ResponseEntity.ok(ApiResponse.success(weeklyReportService.save(weeklyReportDto),"Report saved successfully"));
    }
    @GetMapping("/{studentNo}")
    public ResponseEntity<ApiResponse<List<WeeklyReport>>> getReports(@PathVariable String studentNo){
        return ResponseEntity.ok(ApiResponse.success(weeklyReportService.getAllReportsByStudentNo(studentNo),"Reports fetched successfully"));
    }
    @PutMapping("/update/{reportId}")
    public ResponseEntity<ApiResponse<String>> updateReport(@PathVariable UUID reportId, @RequestBody WeeklyReportDto weeklyReportDto){

            weeklyReportService.update(reportId, weeklyReportDto);
            return ResponseEntity.ok(ApiResponse.success(null,"Report updated successfully"));

    }
    @DeleteMapping("delete/{studentNo}/{reportId}")
    public ResponseEntity<ApiResponse<?>> deleteWeeklyReport(@PathVariable String studentNo, @PathVariable UUID reportId) {

            weeklyReportService.deleteWeeklyReport(studentNo, reportId);
            return ResponseEntity.ok(ApiResponse.success(null, "Report deleted successfully"));

    }
}
