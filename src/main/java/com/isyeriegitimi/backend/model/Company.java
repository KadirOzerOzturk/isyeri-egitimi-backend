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
@Table(name = "firma")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long firmaId;
    private String firmaNo;
    private String firmaEposta;
    private String firmaParola;
    private String firmaAd;
    private String firmaAdres;
    private String firmaLogo;
    private String firmaSektor;
    private String firmaHakkinda;
}
