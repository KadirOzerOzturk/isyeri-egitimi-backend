package com.isyeriegitimi.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "anket_cevap")
public class SurveyAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID cevapId;

    @ManyToOne
    @JoinColumn(name = "anket_id")
    private Survey survey;

    @ManyToOne
    @JoinColumn(name = "soru_id")
    private SurveyQuestion surveyQuestion;

    private UUID userId;
    private String userRole;
    private String answer;
}
