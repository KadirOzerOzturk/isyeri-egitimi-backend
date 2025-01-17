package com.isyeriegitimi.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PreApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID preApplicationId;

    private String studentNumber;
    private String fullName;
    private String idNumber;
    private String email;
    private String phone;
    private String gpa;
    private String preferredTerm;
    private String failedCourses;
    private String companyInfo;
    private String protocolStatus;
    private String mandatoryInternshipDays;
    private String internshipRequestAtCompany;
    private String specialConditions;
}
