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
@Table(name = "form_cevap")
public class FormAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cevapId;

    @ManyToOne
    @JoinColumn(name = "form_id")
    private Form form;

    @ManyToOne
    @JoinColumn(name = "soru_id")
    private FormQuestion formQuestion;

    private Long kullaniciId;
    private String kullaniciRol;
    private String cevap;

}
