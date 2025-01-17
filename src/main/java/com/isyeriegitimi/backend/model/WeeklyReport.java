package com.isyeriegitimi.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "haftalik_rapor")
public class WeeklyReport {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID reportId;
    private String report;
    @ManyToOne
    @JoinColumn(name = "ogrenci_no")
    private Student student;
    private Date reportDate;
}
