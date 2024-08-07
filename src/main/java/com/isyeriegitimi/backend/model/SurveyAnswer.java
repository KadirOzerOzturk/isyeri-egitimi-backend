package com.isyeriegitimi.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "anket_cevap")
public class SurveyAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cevapId;

    @ManyToOne
    @JoinColumn(name = "anket_id")
    private Survey survey;

    @ManyToOne
    @JoinColumn(name = "soru_id")
    private SurveyQuestion surveyQuestion;

    private Long kullaniciId;
    private String kullaniciRol;
    private String cevap;
}
