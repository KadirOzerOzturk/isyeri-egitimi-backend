package com.isyeriegitimi.backend.dto;

import com.isyeriegitimi.backend.model.Announcement;
import com.isyeriegitimi.backend.model.Commission;
import com.isyeriegitimi.backend.model.Company;
import com.isyeriegitimi.backend.model.Student;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationDto {

    private UUID applicationId;
    private Date applicationDate;
    private UUID announcement_id;
    private UUID acceptingCompany;
    private UUID acceptingCommission;
    private UUID student;

    private String pendingRole;

    private String applicationStatus;
}
