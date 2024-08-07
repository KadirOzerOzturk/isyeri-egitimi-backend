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
@Table(name = "ogrenci")
public class Student {
    @Id
    private Long ogrenciNo;
    private String ogrenciAd;
    private String ogrenciSoyad;
    private String ogrenciEposta;
    private String ogrenciTelNo;
    private String ogrenciKimlikNo;
    private String ogrenciAdres;
    private String ogrenciAgno;
    private String ogrenciParola;
    private String ogrenciSinif;

    private String  ogrenciFotograf;
    private String ogrenciFakulte;
    private String ogrenciHakkinda;

    @ManyToOne
    @JoinColumn(name = "firma_id")
    private Company firma;


}


