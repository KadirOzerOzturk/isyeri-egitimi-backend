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
public class CommissionDto {
    private UUID commissionId;
    private String firstName;
    private String lastName;
    private String email;
    private String commissionNumber;
    private String about;
    private String password;
}
