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
@Table(name = "favori_ilan")
public class FavoriteAnnouncement {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID favoriteID;
    @ManyToOne
    @JoinColumn(name = "ogrenci_no")
    private Student student;
    @ManyToOne
    @JoinColumn(name = "ilan_id")
    private Announcement announcement;

}
