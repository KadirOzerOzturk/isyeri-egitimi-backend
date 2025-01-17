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
@Table(name = "anket_soru")
public class SurveyQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID soruId;

    @ManyToOne
    @JoinColumn(name = "anket_id")
    private Survey survey ;

    private int questionNumber;
    private String questionText;

}