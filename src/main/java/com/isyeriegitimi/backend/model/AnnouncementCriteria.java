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
@Table(name = "announcement_criteria")
public class AnnouncementCriteria {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID criteriaId;

    @ManyToOne
    @JoinColumn(name = "announcement_id")
    private Announcement announcement;

    private String criteriaDescription;
}
