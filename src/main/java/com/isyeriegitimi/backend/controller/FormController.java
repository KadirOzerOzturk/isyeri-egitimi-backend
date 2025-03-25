package com.isyeriegitimi.backend.controller;


import com.isyeriegitimi.backend.dto.FormDto;
import com.isyeriegitimi.backend.model.*;
import com.isyeriegitimi.backend.security.enums.Role;
import com.isyeriegitimi.backend.service.FormService;
import com.isyeriegitimi.backend.service.FormSignatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/forms")
public class FormController {
    @Autowired
    private FormService formService;
    @Autowired
    private  FormSignatureService formSignatureService;


    @PostMapping("/sign")
    public ResponseEntity<ApiResponse<String>> signForm(@RequestBody SignFormRequest request) {
        String message = formSignatureService.signForm(request.getFormId(), request.getUserId(), String.valueOf(request.getUserRole()),request.getStudentId());
        return ResponseEntity.ok(ApiResponse.success(message, "Form signed successfully."));
    }

    @GetMapping("/signed")
    public ResponseEntity<ApiResponse<List<FormSignature>>> getSignedForms() {
        return ResponseEntity.ok(ApiResponse.success(formService.getSignedForms(), "Signed forms fetched successfully."));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Form>>> getAllForms() {
        return ResponseEntity.ok(ApiResponse.success(formService.getAllForms(), "Forms fetched successfully."));
    }

    @GetMapping("/signed/{userId}/{userRole}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSignedFormsById(@PathVariable UUID userId, @PathVariable String userRole) {
        Map<String, Object> response = formService.getSignedAndUnsignedForms(userId, userRole);
        return ResponseEntity.ok(ApiResponse.success(response, "Signed and unsigned forms fetched successfully."));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Form>> getFormById( @PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(formService.getFormById(id), "Forms fetched successfully."));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Form>> createForm(@RequestBody CreateFormRequest request) {
        Form createdForm = formService.createForm(request.getForm(), request.getSignatureRoles());

        return ResponseEntity.ok(ApiResponse.success(createdForm, "Form created successfully."));
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
    @GetMapping("/signed/{userId}/{userRole}/{studentId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSignedFormsByStudentId(@PathVariable UUID userId, @PathVariable String userRole, @PathVariable UUID studentId) {
        Map<String, Object> response =formService.getSignedFormsByStudentId(userId, userRole, studentId);
        return ResponseEntity.ok(ApiResponse.success(response, "Signed forms fetched successfully."));
    }

}
