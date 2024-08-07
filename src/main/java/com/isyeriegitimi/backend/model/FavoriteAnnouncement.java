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
@Table(name = "favori_ilan")
public class FavoriteAnnouncement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long favoriId;
    @ManyToOne
    @JoinColumn(name = "ogrenci_no")
    private Student ogrenci;
    @ManyToOne
    @JoinColumn(name = "ilan_id")
    private Announcement ilan;

}
