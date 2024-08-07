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
@Table(name = "izleyici")
public class Lecturer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long izleyiciId;
    private String izleyiciAd;
    private String izleyiciSoyad;
    private String izleyiciEposta;
    private String izleyiciParola;
    private String izleyiciFakulte;
    private String izleyiciHakkinda;

    private String izleyiciNo;
}
