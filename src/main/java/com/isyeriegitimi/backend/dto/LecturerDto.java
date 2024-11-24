package com.isyeriegitimi.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LecturerDto {
    private Long izleyiciId;
    private String izleyiciAd;
    private String izleyiciSoyad;
    private String izleyiciEposta;
    private String izleyiciParola;
    private String izleyiciFakulte;
    private String izleyiciHakkinda;
    private String izleyiciNo;
}
