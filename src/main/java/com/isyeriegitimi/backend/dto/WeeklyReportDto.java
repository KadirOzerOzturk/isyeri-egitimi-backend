package com.isyeriegitimi.backend.dto;

import com.isyeriegitimi.backend.model.Student;
import lombok.Data;

import java.util.Date;

@Data
public class WeeklyReportDto {
    private Long reportId;
    private String report;

    private Student student;
    private Date reportDate;
}
