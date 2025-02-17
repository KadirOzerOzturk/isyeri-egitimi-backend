package com.isyeriegitimi.backend.dto;

import com.isyeriegitimi.backend.model.Survey;
import com.isyeriegitimi.backend.model.SurveyQuestion;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@RequiredArgsConstructor
public class SurveyAnswerDto {
    private UUID answerId;
    private Survey survey;
    private SurveyQuestion surveyQuestion;
    private UUID userId;
    private String userRole;
    private String answer;
}
