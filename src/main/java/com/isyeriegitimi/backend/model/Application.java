package com.isyeriegitimi.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "basvuru")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long basvuruId;
    private Date basvuruTarihi;

    @ManyToOne
    @JoinColumn(name = "ilan_id")
    private Announcement ilan;

    @ManyToOne
    @JoinColumn(name = "firma_id")
    private Company firma;
    @ManyToOne
    @JoinColumn(name = "ogrenci_no")
    private Student ogrenci;
    private String basvuruDurum;


}
