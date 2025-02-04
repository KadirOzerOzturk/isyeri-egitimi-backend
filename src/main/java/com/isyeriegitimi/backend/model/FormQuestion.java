package com.isyeriegitimi.backend.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.isyeriegitimi.backend.converter.JsonNodeConverter;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "form_question")
public class FormQuestion implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID questionId;

    @ManyToOne
    @JoinColumn(name = "form_id")
    private Form form;

    private int questionNumber;
    private String questionText;

    @Column(name = "options", columnDefinition = "jsonb")
    @Convert(converter = JsonNodeConverter.class)
    private JsonNode options;
}
