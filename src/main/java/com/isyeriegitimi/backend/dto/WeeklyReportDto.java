package com.isyeriegitimi.backend.dto;

import com.isyeriegitimi.backend.model.Student;
import lombok.Data;

import java.util.Date;

@Data
public class WeeklyReportDto {
    private Long raporId;
    private String raporIcerigi;

    private Student ogrenci;
    private Date tarih;
}
