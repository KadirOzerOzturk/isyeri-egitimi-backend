package com.isyeriegitimi.backend.controller;

import com.isyeriegitimi.backend.dto.SurveyDto;
import com.isyeriegitimi.backend.model.ApiResponse;
import com.isyeriegitimi.backend.model.Survey;
import com.isyeriegitimi.backend.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/surveys")
public class SurveyController {
    @Autowired
    private SurveyService surveyService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Survey>>> getAllSurveys() {
        return ResponseEntity.ok(ApiResponse.success(surveyService.getAllSurveys(), "Surveys fetched successfully."));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Survey>> getSurveyById( @PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(surveyService.getSurveyById(id), "Surveys fetched successfully."));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Survey>> createSurvey(@RequestBody Survey survey) {
        return ResponseEntity.ok(ApiResponse.success(surveyService.createSurvey(survey), "Survey created successfully."));
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Survey>> updateSurvey(@PathVariable UUID id, @RequestBody SurveyDto surveyDto) {

        return ResponseEntity.ok(ApiResponse.success(surveyService.updateSurvey(surveyDto,id), "Survey updated successfully."));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSurvey(@PathVariable UUID id) {
        surveyService.deleteSurvey(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Survey deleted successfully."));
    }

}
