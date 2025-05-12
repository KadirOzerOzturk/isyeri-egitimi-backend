package com.isyeriegitimi.backend.model;

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
@Table(name = "announcement")
public class Announcement {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID announcementId;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    private Date startDate;
    private Date endDate;
    private String title;
    @Column(length = 3000)
    private String description;
    private String postTitle;
    private String no;

}
