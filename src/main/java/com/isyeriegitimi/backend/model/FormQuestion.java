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
@Table(name = "form_question")
public class FormQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID questionId;

    @ManyToOne
    @JoinColumn(name = "form_id")
    private Form form;

    private int questionNumber;
    private String questionText;
}
