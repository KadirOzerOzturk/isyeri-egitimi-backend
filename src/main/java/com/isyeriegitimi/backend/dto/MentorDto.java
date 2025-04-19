package com.isyeriegitimi.backend.dto;


import com.isyeriegitimi.backend.model.Company;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MentorDto {

    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Company company;

}