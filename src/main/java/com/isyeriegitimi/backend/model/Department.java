package com.isyeriegitimi.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "department")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID departmentId;

    private String departmentName;
}
