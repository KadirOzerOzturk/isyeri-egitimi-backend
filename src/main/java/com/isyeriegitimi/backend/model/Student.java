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
@Table(name = "student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID studentId;

    @Column(name = "student_number",  unique = true)
    private String studentNumber;

    @Column(name = "first_name" )
    private String firstName;

    @Column(name = "last_name" )
    private String lastName;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "identity_number", unique = true)
    private String identityNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "gpa")
    private String gpa;

    @Column(name = "password" )
    private String password;

    @Column(name = "grade")
    private String grade;

    @Column(name = "faculty")
    private String faculty;

    @Column(name = "about", length = 1000)
    private String about;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = true)
    private Company company;
    @ManyToOne
    @JoinColumn(name = "mentor_id", nullable = true)
    private Mentor mentor;
}
