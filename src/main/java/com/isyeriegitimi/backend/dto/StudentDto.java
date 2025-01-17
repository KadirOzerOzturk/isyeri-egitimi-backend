package com.isyeriegitimi.backend.dto;

import com.isyeriegitimi.backend.model.Company;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto {
    private String studentNumber;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String gpa;
    private String grade;
    private String faculty;
    private String about;

    private Company company;
}
