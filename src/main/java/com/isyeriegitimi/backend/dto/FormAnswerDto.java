package com.isyeriegitimi.backend.dto;

import com.isyeriegitimi.backend.model.Form;
import com.isyeriegitimi.backend.model.FormQuestion;
import jakarta.persistence.Entity;
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
public class FormAnswerDto {
    private UUID answerId;
    private Form form;
    private FormQuestion formQuestion;
    private UUID userId;
    private String userRole;
    private String answer;
}
