package com.isyeriegitimi.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommissionDto {
    private Long komisyonId;
    private String komisyonEposta;
    private String komisyonParola;
    private String komisyonAd;
    private String komisyonSoyad;
    private String komisyonNo;
    private String komisyonHakkinda;
}
