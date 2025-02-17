package com.isyeriegitimi.backend.controller;

import com.isyeriegitimi.backend.model.ApiResponse;
import com.isyeriegitimi.backend.model.FormAnswer;
import com.isyeriegitimi.backend.service.FormAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/forms/answers")
public class FormAnswerController {

    @Autowired
    private FormAnswerService formAnswerService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<FormAnswer>>> getAllAnswers() {
        return ResponseEntity.ok(ApiResponse.success(formAnswerService.getAllAnswers(), "Answers fetched successfully."));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<List<FormAnswer>>> getAnswerById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(formAnswerService.getAnswerByFormId(id), "Answer fetched successfully."));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<FormAnswer>> submitAnswer(@RequestBody FormAnswer answer) {
        return ResponseEntity.ok(ApiResponse.success(formAnswerService.submitAnswer(answer), "Answer submitted successfully."));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FormAnswer>> updateAnswer(@PathVariable UUID id, @RequestBody FormAnswer updatedAnswer) {
        return ResponseEntity.ok(ApiResponse.success(formAnswerService.updateAnswer(id, updatedAnswer), "Answer updated successfully."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAnswer(@PathVariable UUID id) {
        formAnswerService.deleteAnswer(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Answer deleted successfully."));
    }
    @PostMapping("/submit")
    public ResponseEntity<ApiResponse<?>> submitAnswers(@RequestBody List<FormAnswer> answers) {
        formAnswerService.saveAnswers(answers);
        return ResponseEntity.ok(ApiResponse.success(null,"Form cevapları başarıyla kaydedildi."));
    }
}