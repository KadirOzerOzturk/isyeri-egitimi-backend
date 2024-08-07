package com.isyeriegitimi.backend.dto;

import com.isyeriegitimi.backend.model.Company;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentDto {
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
    private String ogrenciFotograf;
    private String ogrenciFakulte;
    private String ogrenciHakkinda;
    private Company firma;
}
