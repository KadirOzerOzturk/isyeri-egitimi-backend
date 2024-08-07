package com.isyeriegitimi.backend.controller;


import com.isyeriegitimi.backend.dto.CommissionDto;
import com.isyeriegitimi.backend.service.CommissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/commission")
public class CommissionController {

    private final CommissionService commissionService;

    @Autowired
    public CommissionController(CommissionService commissionService) {
        this.commissionService = commissionService;
    }


    @GetMapping("/{commissionId}")
    public ResponseEntity<?> getCommissionById(@PathVariable Long  commissionId){

        try {
            return ResponseEntity.ok(commissionService.getCommissionById(commissionId));
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @PutMapping("/update/{commissionId}")
    public ResponseEntity<?> updateCommission(@PathVariable Long commissionId,@RequestBody CommissionDto commissionDto){
        try {
            commissionService.update(commissionId,commissionDto);
            return ResponseEntity.ok("Successfully Updated");
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
