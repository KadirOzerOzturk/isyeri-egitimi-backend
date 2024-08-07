package com.isyeriegitimi.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PreApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ogrenciNumarasi;
    private String adSoyad;
    private String tckNumarasi;
    private String eposta;
    private String telefon;
    private String genelNotOrtalamasi;
    private String tercihEdilenDonem;
    private String basarisizDersler;
    private String sirketBilgisi;
    private String protokolDurumu;
    private String zorunluStajGunSayisi;
    private String firmadaStajYapmaIstegi;
    private String ozelDurumlar;
}
