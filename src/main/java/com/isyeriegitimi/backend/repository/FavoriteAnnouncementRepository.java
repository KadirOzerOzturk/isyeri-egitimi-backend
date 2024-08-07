package com.isyeriegitimi.backend.repository;

import com.isyeriegitimi.backend.model.FavoriteAnnouncement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteAnnouncementRepository extends JpaRepository<FavoriteAnnouncement,Long> {

    List<FavoriteAnnouncement> findByOgrenci_OgrenciNo(Long studentNo);
    Optional<FavoriteAnnouncement> findByOgrenci_OgrenciNoAndIlan_IlanId(Long studentNo, Long ilanId);
}
