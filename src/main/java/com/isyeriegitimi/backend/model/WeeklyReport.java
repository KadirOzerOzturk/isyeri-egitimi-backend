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
@Table(name = "haftalik_rapor")
public class WeeklyReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long raporId;
    private String raporIcerigi;
    @ManyToOne
    @JoinColumn(name = "ogrenci_no")
    private Student ogrenci;
    private Date tarih;
}
