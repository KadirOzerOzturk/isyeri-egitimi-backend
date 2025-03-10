package com.isyeriegitimi.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.isyeriegitimi.backend.model.ApiResponse;
import com.isyeriegitimi.backend.model.FormQuestion;
import com.isyeriegitimi.backend.service.FormQuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/forms/questions")
public class FormQuestionController {

    @Autowired
    private FormQuestionService formQuestionService;
    private static final Logger logger = LoggerFactory.getLogger(FormQuestionController.class);

    @GetMapping
    public ResponseEntity<ApiResponse<List<FormQuestion>>> getAllQuestions() {
        return ResponseEntity.ok(ApiResponse.success(formQuestionService.getAllQuestions(), "Questions fetched successfully."));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<List<FormQuestion>>> getQuestionsByFormId(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(formQuestionService.getQuestionsByFormId(id), "Questions fetched successfully."));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UUID>> createQuestion(@RequestBody FormQuestion question) throws JsonProcessingException {
        logger.info("Recieved request to create question {}", question);

        return ResponseEntity.ok(ApiResponse.success(formQuestionService.createQuestion(question), "Question created successfully."));
    }
    @PostMapping("/bulk/{formId}")
    public ResponseEntity<ApiResponse<?>> bulkCreate(@RequestBody List<FormQuestion> questions,@PathVariable UUID formId){
        formQuestionService.bulkSave(questions,formId);
        return ResponseEntity.ok(ApiResponse.success(null,"Questions created successfully"));

    }


    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FormQuestion>> updateQuestion(@PathVariable UUID id, @RequestBody FormQuestion updatedQuestion) {
        return ResponseEntity.ok(ApiResponse.success(formQuestionService.updateQuestion(id, updatedQuestion), "Question updated successfully."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteQuestion(@PathVariable UUID id) {
        formQuestionService.deleteQuestion(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Question deleted successfully."));
    }
}
