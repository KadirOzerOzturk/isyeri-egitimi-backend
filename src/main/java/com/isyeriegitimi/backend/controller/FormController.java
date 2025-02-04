package com.isyeriegitimi.backend.controller;


import com.isyeriegitimi.backend.dto.FormDto;
import com.isyeriegitimi.backend.model.ApiResponse;
import com.isyeriegitimi.backend.model.Form;
import com.isyeriegitimi.backend.service.FormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/forms")
public class FormController {
    @Autowired
    private FormService formService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Form>>> getAllForms() {
        return ResponseEntity.ok(ApiResponse.success(formService.getAllForms(), "Forms fetched successfully."));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Form>> getFormById( @PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(formService.getFormById(id), "Forms fetched successfully."));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Form>> createForm(@RequestBody Form form) {
        return ResponseEntity.ok(ApiResponse.success(formService.createForm(form), "Form created successfully."));
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Form>> updateForm(@PathVariable UUID id, @RequestBody FormDto formDto) {

        return ResponseEntity.ok(ApiResponse.success(formService.updateForm(formDto,id), "Form updated successfully."));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteForm(@PathVariable UUID id) {
        formService.deleteForm(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Form deleted successfully."));
    }
}
