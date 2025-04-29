package com.isyeriegitimi.backend.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.isyeriegitimi.backend.converter.JsonNodeConverter;
import com.isyeriegitimi.backend.enums.QuestionType;
import com.isyeriegitimi.backend.model.Form;
import jakarta.persistence.*;
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
    private Form form;
    private int questionNumber;
    private String questionText;
    private JsonNode options;
    private QuestionType questionTypeEnum;
    private String questionType;
    private String requiredFor;
}
