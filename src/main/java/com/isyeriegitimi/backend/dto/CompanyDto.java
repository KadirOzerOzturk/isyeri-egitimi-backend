package com.isyeriegitimi.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDto {
    private UUID companyId;
    private String companyNumber;
    private String name;
    private String email;
    private String address;
    private String sector;
    private String about;
    private String logo;
}
