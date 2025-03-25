package com.isyeriegitimi.backend.model;

import com.isyeriegitimi.backend.security.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "form_signature")
public class FormSignature {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "form_id", nullable = false)
    private Form form; // İmzalanan form



    private UUID signedBy;
    private String signedByRole;

    private UUID studentId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date signedAt;
}
