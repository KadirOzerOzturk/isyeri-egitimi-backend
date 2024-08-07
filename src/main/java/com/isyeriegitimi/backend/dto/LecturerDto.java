package com.isyeriegitimi.backend.dto;

import lombok.Data;

@Data
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
