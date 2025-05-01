package com.isyeriegitimi.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FormAnswerRequest {
    private UUID formId;
    private UUID questionId;
    private UUID userId;
    private String userRole;
    private String answer;
    private UUID studentId;
}
