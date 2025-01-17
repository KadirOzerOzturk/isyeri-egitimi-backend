package com.isyeriegitimi.backend.dto;

import com.isyeriegitimi.backend.model.Company;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class AnnouncementDto {


    private Company company;
    private Date startDate;
    private Date endDate;
    private String title;
    private String description;
    private String postTitle;
    private List<String> announcementCriteria;
}
