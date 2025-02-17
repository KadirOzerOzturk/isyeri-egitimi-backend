package com.isyeriegitimi.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.isyeriegitimi.backend.dto.SurveyQuestionDto;
import com.isyeriegitimi.backend.model.ApiResponse;
import com.isyeriegitimi.backend.model.SurveyQuestion;
import com.isyeriegitimi.backend.model.SurveyQuestion;
import com.isyeriegitimi.backend.service.SurveyQuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/surveys/questions")
public class SurveyQuestionController {
    @Autowired
    private SurveyQuestionService surveyQuestionService;
    private static final Logger logger = LoggerFactory.getLogger(SurveyQuestionController.class);

    @PostMapping
    public ResponseEntity<ApiResponse<UUID>> createQuestion(@RequestBody SurveyQuestion question) throws JsonProcessingException {
        logger.info("Recieved request to create question {}", question);

        return ResponseEntity.ok(ApiResponse.success(surveyQuestionService.createQuestion(question), "Question created successfully."));
    }
    @PostMapping("/bulk/{announcementId}")
    public ResponseEntity<ApiResponse<?>> createQuestions(@RequestBody List<SurveyQuestion> questions, @PathVariable UUID announcementId) throws JsonProcessingException {
        logger.info("Recieved request to create questions {}", questions);
        surveyQuestionService.createQuestions(questions);
        return ResponseEntity.ok(ApiResponse.success(null, "Question created successfully."));
    }
    @GetMapping
    public ResponseEntity<ApiResponse<List<SurveyQuestion>>> getAllQuestions() {
        return ResponseEntity.ok(ApiResponse.success(surveyQuestionService.getAllQuestions(), "Questions fetched successfully."));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<List<SurveyQuestion>>> getQuestionsBySurveyId(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(surveyQuestionService.getQuestionsBySurveyId(id), "Questions fetched successfully."));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SurveyQuestion>> updateQuestion(@PathVariable UUID id, @RequestBody SurveyQuestion updatedQuestion) {
        return ResponseEntity.ok(ApiResponse.success(surveyQuestionService.updateQuestion(id, updatedQuestion), "Question updated successfully."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteQuestion(@PathVariable UUID id) {
        surveyQuestionService.deleteQuestion(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Question deleted successfully."));
    }
}
