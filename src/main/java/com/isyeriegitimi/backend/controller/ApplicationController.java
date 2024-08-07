package com.isyeriegitimi.backend.controller;


import com.isyeriegitimi.backend.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/applications")
public class ApplicationController {

    private ApplicationService applicationService;

    @Autowired
    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping("/{announcementId}")
    public ResponseEntity<?>  getApplicationsByAnnouncementId(@PathVariable Long announcementId){
        try {
            return ResponseEntity.ok(applicationService.getApplicationsByAnnouncementId(announcementId));
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/student/{studentNo}")
    public ResponseEntity<?>  getApplicationsByStudentNo(@PathVariable Long studentNo){
        try {
            return ResponseEntity.ok(applicationService.getApplicationsByStudentNo(studentNo));
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/student/{studentNo}/{companyId}")
    public ResponseEntity<?>  getApplicationsByStudentNoAndCompanyId(@PathVariable Long studentNo,@PathVariable Long companyId){
        try {
            return ResponseEntity.ok(applicationService.getApplicationsByStudentNoAndCompanyId(studentNo,companyId));
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/apply/{studentNo}/{announcementId}")
    public ResponseEntity<?> applyToCompany(@PathVariable Long studentNo,@PathVariable Long announcementId){
        try {
            return ResponseEntity.ok(applicationService.saveApplication(studentNo,announcementId));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body("Save failed");

        }
    }
    @PutMapping("/update/{applicationId}/{applicationStatus}")
    public ResponseEntity<?> updateApplication( @PathVariable String applicationStatus,@PathVariable Long applicationId) {
        try {
            
            String result = applicationService.updateApplication( applicationStatus, applicationId);
            if ("Not Found".equals(result)) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.ok(result);
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Update failed");
        }
    }

    @DeleteMapping("/delete/{studentNo}/{announcementId}")
    public ResponseEntity<?> deleteApplication(@PathVariable Long studentNo,@PathVariable Long announcementId){

        try {
            return ResponseEntity.ok(applicationService.deleteApplication(studentNo,announcementId));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body("Delete failed");

        }
    }

}
