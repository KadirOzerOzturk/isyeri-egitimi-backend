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
@Table(name = "komisyon")
public class Commission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long komisyonId;
    private String komisyonEposta;
    private String komisyonParola;
    private String komisyonAd;
    private String komisyonSoyad;
    private String komisyonNo;
    private String komisyonHakkinda;
}
