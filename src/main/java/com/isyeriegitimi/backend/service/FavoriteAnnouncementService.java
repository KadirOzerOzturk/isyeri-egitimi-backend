package com.isyeriegitimi.backend.service;

import com.isyeriegitimi.backend.dto.FavoriteAnnouncementDto;
import com.isyeriegitimi.backend.model.FavoriteAnnouncement;
import com.isyeriegitimi.backend.model.Student;
import com.isyeriegitimi.backend.repository.FavoriteAnnouncementRepository;
import com.isyeriegitimi.backend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class FavoriteAnnouncementService {


    private FavoriteAnnouncementRepository favoriteAnnouncementRepository;
    private StudentRepository studentRepository;
    @Autowired
    public FavoriteAnnouncementService(FavoriteAnnouncementRepository favoriteAnnouncementRepository, StudentRepository studentRepository) {
        this.favoriteAnnouncementRepository = favoriteAnnouncementRepository;
        this.studentRepository = studentRepository;
    }



    public List<FavoriteAnnouncement> getFavoriteAnnouncements(Long studentNo){
        List<FavoriteAnnouncement> favorites = favoriteAnnouncementRepository.findAll();

        Collections.sort(favorites, new Comparator<FavoriteAnnouncement>() {
            @Override
            public int compare(FavoriteAnnouncement f1, FavoriteAnnouncement f2) {
                return f2.getIlan().getBaslangic_tarihi().compareTo(f1.getIlan().getBaslangic_tarihi());
            }
        });
        return favorites;
    }

    public void save(FavoriteAnnouncementDto favoriteAnnouncementDto){
        try{
            Student student=studentRepository.findByOgrenciNo(favoriteAnnouncementDto.getOgrenci().getOgrenciNo()).orElseThrow(()-> new RuntimeException("User Not Found"));

            FavoriteAnnouncement favoriteAnnouncement=FavoriteAnnouncement
                    .builder()
                    .ogrenci(student)
                    .favoriId(favoriteAnnouncementDto.getFavoriId())
                    .ilan(favoriteAnnouncementDto.getIlan())
                    .build();
            favoriteAnnouncementRepository.save(favoriteAnnouncement);
        }
        catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Error saving favorite", e);
        }
    }

    public void deleteFavoriteAnnouncement(Long studentNo, Long announcementId) {
        try {
            // Favori ilanı bul
            FavoriteAnnouncement favoriteAnnouncement = favoriteAnnouncementRepository.findByOgrenci_OgrenciNoAndIlan_IlanId(studentNo, announcementId)
                    .orElseThrow(() -> new RuntimeException("Favorite announcement not found"));

            // Favoriyi sil
            favoriteAnnouncementRepository.delete(favoriteAnnouncement);
        } catch (Exception e) {
            throw new RuntimeException("Error removing favorite", e);
        }
    }


}
