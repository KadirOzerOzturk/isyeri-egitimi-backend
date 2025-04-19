package com.isyeriegitimi.backend.controller;


import com.isyeriegitimi.backend.model.ApiResponse;
import com.isyeriegitimi.backend.model.Application;
import com.isyeriegitimi.backend.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/applications")
public class ApplicationController {

    private ApplicationService applicationService;

    @Autowired
    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }
    @GetMapping
    public ResponseEntity<ApiResponse<List<Application>>> getAllApplications() {
        return ResponseEntity.ok(ApiResponse.success(applicationService.getAllApplications(),"Applications fetched successfully"));
    }
    @GetMapping("/{announcementId}")
    public ResponseEntity<ApiResponse<?>>  getApplicationsByAnnouncementId(@PathVariable UUID announcementId){

        return ResponseEntity.ok(ApiResponse.success(applicationService.getApplicationsByAnnouncementId(announcementId), "Applications fetched successfully."));
    }
    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<?>>  getApplicationsByStudentId(@PathVariable UUID studentId){
        return ResponseEntity.ok(ApiResponse.success(applicationService.getApplicationsByStudentId(studentId), "Applications fetched successfully."));
    }
    @GetMapping("/student/{studentId}/{companyId}")
    public ResponseEntity<ApiResponse<?>>  getApplicationsByStudentNoAndCompanyId(@PathVariable UUID studentId,@PathVariable UUID companyId){
        return ResponseEntity.ok(ApiResponse.success(applicationService.getApplicationsByStudentIdAndCompanyId(studentId,companyId), "Applications fetched successfully."));
    }
    @PostMapping("/apply/{studentId}/{announcementId}")
    public ResponseEntity<ApiResponse<Void>> applyToCompany(@PathVariable UUID studentId,@PathVariable UUID announcementId){
        applicationService.saveApplication(studentId, announcementId);
        return ResponseEntity.ok(ApiResponse.success(null, "Application saved successfully."));
    }
    @PutMapping("/update/{applicationId}/{pendingRole}")
    public ResponseEntity<ApiResponse<?>> updateApplication( @PathVariable String pendingRole,@PathVariable UUID applicationId) {
        applicationService.updateApplication( pendingRole, applicationId);
        return ResponseEntity.ok(ApiResponse.success(null, "Application updated successfully."));
    }

    @DeleteMapping("/delete/{studentNo}/{announcementId}")
    public ResponseEntity<ApiResponse<?>> deleteApplication(@PathVariable String studentNo,@PathVariable UUID announcementId){
        applicationService.deleteApplication(studentNo,announcementId);
        return ResponseEntity.ok(ApiResponse.success(null, "Application deleted successfully."));
    }
    @GetMapping("/pending/{confirmingRole}")
    public ResponseEntity<ApiResponse<List<Application>>> getPendingApplications(@PathVariable String confirmingRole){
        return ResponseEntity.ok(ApiResponse.success(applicationService.getPendingApplicationsByRole(confirmingRole),"Applications fetched successfully."));
    }


}
