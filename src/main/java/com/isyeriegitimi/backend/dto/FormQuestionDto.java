package com.isyeriegitimi.backend.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FormQuestionDto {

    private UUID questionId;
    private UUID formId;
    private int questionNumber;
    private String questionText;
    private JsonNode options;

    private String questionType;

    private String requiredFor;
}
