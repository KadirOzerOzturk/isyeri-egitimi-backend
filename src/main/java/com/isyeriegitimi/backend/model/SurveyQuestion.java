package com.isyeriegitimi.backend.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.isyeriegitimi.backend.converter.JsonNodeConverter;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "survey_question")
public class SurveyQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID questionId;

    @ManyToOne
    @JoinColumn(name = "survey_id")
    private Survey survey ;

    private int questionNumber;
    private String questionText;
    private String questionType;
    @Column(name = "options", columnDefinition = "jsonb")
    @Convert(converter = JsonNodeConverter.class)
    private JsonNode options;
}