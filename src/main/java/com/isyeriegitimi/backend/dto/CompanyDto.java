package com.isyeriegitimi.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDto {
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
