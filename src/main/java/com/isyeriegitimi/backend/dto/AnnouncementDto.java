package com.isyeriegitimi.backend.dto;

import com.isyeriegitimi.backend.model.Company;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class AnnouncementDto {


    private Company firma;
    private Date baslangic_tarihi;
    private Date bitis_tarihi;
    private String baslik;
    private String aciklama;
    private String postBaslik;
    private List<String> announcementCriteria;
}
