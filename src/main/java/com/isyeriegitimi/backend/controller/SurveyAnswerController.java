package com.isyeriegitimi.backend.controller;

import com.isyeriegitimi.backend.dto.SurveyAnswerDto;
import com.isyeriegitimi.backend.model.ApiResponse;
import com.isyeriegitimi.backend.service.SurveyAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/surveys/answers")
public class SurveyAnswerController {

    @Autowired
    private SurveyAnswerService surveyAnswerService;


    @PostMapping("/submit")
    public ResponseEntity<ApiResponse<?>> saveAnswers(@RequestBody List<SurveyAnswerDto> answerDtoList) {
        surveyAnswerService.saveAnswers(answerDtoList);
        return ResponseEntity.ok(ApiResponse.success(null, "Answers saved successfully"));
    }
    @PutMapping("/update")
    public ResponseEntity<ApiResponse<?>> updateAnswers(@RequestBody List<SurveyAnswerDto> answerDtoList) {
        surveyAnswerService.updateAnswers(answerDtoList);
        return ResponseEntity.ok(ApiResponse.success(null, "Answers updated successfully"));
    }
    @GetMapping("/{surveyId}/{userId}")
    public ResponseEntity<ApiResponse<?>> getAnswersBySurveyAndUser(@PathVariable UUID surveyId, @PathVariable UUID userId) {
        return ResponseEntity.ok(ApiResponse.success(surveyAnswerService.getAnswersBySurveyAndUser(surveyId, userId), "Answers fetched successfully"));
    }

}
