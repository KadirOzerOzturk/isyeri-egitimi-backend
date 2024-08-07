package com.isyeriegitimi.backend.dto;

import lombok.Data;

@Data
public class CommissionDto {
    private Long komisyonId;
    private String komisyonEposta;
    private String komisyonParola;
    private String komisyonAd;
    private String komisyonSoyad;
    private String komisyonNo;
    private String komisyonHakkinda;
}
