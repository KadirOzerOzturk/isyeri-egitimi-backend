package com.isyeriegitimi.backend.controller;


import com.isyeriegitimi.backend.dto.CommissionDto;
import com.isyeriegitimi.backend.model.ApiResponse;
import com.isyeriegitimi.backend.service.CommissionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/commissions")
public class CommissionController {

    private final CommissionService commissionService;

    @Autowired
    public CommissionController(CommissionService commissionService) {
        this.commissionService = commissionService;
    }


    @GetMapping("/{commissionId}")
    public ResponseEntity<ApiResponse<?>> getCommissionById(@PathVariable @Valid UUID commissionId){

        return ResponseEntity.ok(ApiResponse.success(commissionService.getCommissionById(commissionId),"Commission fetched successfully"));
    }
    @PutMapping("/update/{commissionId}")
    public ResponseEntity<ApiResponse<?>> updateCommission(@PathVariable UUID commissionId,@RequestBody CommissionDto commissionDto){
        commissionService.update(commissionId,commissionDto);
        return ResponseEntity.ok(ApiResponse.success(null,"Commission updated successfully"));

    }
    @PostMapping("/save")
    public ResponseEntity<ApiResponse<?>> saveCommission(@RequestBody CommissionDto commissionDto){
        ;
        return ResponseEntity.ok(ApiResponse.success(commissionService.save(commissionDto),"Commission saved successfully"));
    }
}
