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
@Table(name = "form_soru")
public class FormQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long soruId;

    @ManyToOne
    @JoinColumn(name = "form_id")
    private Form form ;

    private int soruNo;
    private String soruMetni;
}
