package com.isyeriegitimi.backend.model;

import com.isyeriegitimi.backend.security.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "application")
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID applicationId;

    private Date applicationDate;

    @ManyToOne
    @JoinColumn(name = "announcement_id")
    private Announcement announcement;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    private String  pendingRole;

    private String applicationStatus;
}
