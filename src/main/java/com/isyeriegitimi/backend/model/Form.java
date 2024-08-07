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
@Table(name = "form")
public class Form{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long formId;
    private String formBaslik;
    private String formAciklama;

}
