package com.isyeriegitimi.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ilan_kriter")
public class AnnouncementCriteria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long kriterId;

    @ManyToOne
    @JoinColumn(name = "ilan_id" )
    private Announcement ilan;

    private String kriterAciklama;
}
