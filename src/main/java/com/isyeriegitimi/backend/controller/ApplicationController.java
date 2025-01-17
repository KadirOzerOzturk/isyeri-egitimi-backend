package com.isyeriegitimi.backend.controller;


import com.isyeriegitimi.backend.model.ApiResponse;
import com.isyeriegitimi.backend.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/applications")
public class ApplicationController {

    private ApplicationService applicationService;

    @Autowired
    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping("/{announcementId}")
    public ResponseEntity<ApiResponse<?>>  getApplicationsByAnnouncementId(@PathVariable UUID announcementId){

        return ResponseEntity.ok(ApiResponse.success(applicationService.getApplicationsByAnnouncementId(announcementId), "Applications fetched successfully."));
    }
    @GetMapping("/student/{studentNo}")
    public ResponseEntity<ApiResponse<?>>  getApplicationsByStudentNo(@PathVariable String studentNo){
        return ResponseEntity.ok(ApiResponse.success(applicationService.getApplicationsByStudentNo(studentNo), "Applications fetched successfully."));
    }
    @GetMapping("/student/{studentNo}/{companyId}")
    public ResponseEntity<ApiResponse<?>>  getApplicationsByStudentNoAndCompanyId(@PathVariable String studentNo,@PathVariable UUID companyId){
        return ResponseEntity.ok(ApiResponse.success(applicationService.getApplicationsByStudentNoAndCompanyId(studentNo,companyId), "Applications fetched successfully."));
    }
    @PostMapping("/apply/{studentNo}/{announcementId}")
    public ResponseEntity<ApiResponse<Void>> applyToCompany(@PathVariable String studentNo,@PathVariable UUID announcementId){
        applicationService.saveApplication(studentNo, announcementId);
        return ResponseEntity.ok(ApiResponse.success(null, "Application saved successfully."));
    }
    @PutMapping("/update/{applicationId}/{applicationStatus}")
    public ResponseEntity<ApiResponse<?>> updateApplication( @PathVariable String applicationStatus,@PathVariable UUID applicationId) {
        applicationService.updateApplication( applicationStatus, applicationId);
        return ResponseEntity.ok(ApiResponse.success(null, "Application updated successfully."));
    }

    @DeleteMapping("/delete/{studentNo}/{announcementId}")
    public ResponseEntity<ApiResponse<?>> deleteApplication(@PathVariable String studentNo,@PathVariable UUID announcementId){
        applicationService.deleteApplication(studentNo,announcementId);
        return ResponseEntity.ok(ApiResponse.success(null, "Application deleted successfully."));
    }

}
