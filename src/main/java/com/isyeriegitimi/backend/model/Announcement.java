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
@Table(name = "ilan")
public class Announcement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ilanId;
    @ManyToOne
    @JoinColumn(name = "firma_id")
    private Company firma;
    private Date baslangic_tarihi;
    private Date bitis_tarihi;
    private String baslik;
    private String aciklama;
    private String postBaslik;
}
