package com.isyeriegitimi.backend.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.isyeriegitimi.backend.converter.JsonNodeConverter;
import com.isyeriegitimi.backend.model.Survey;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SurveyQuestionDto {
    private UUID questionId;


    private Survey survey ;

    private int questionNumber;
    private String questionText;
    private String questionType;

    private JsonNode options;
}
