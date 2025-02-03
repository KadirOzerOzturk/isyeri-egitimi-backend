package com.isyeriegitimi.backend.service;

import com.isyeriegitimi.backend.dto.FavoriteAnnouncementDto;
import com.isyeriegitimi.backend.exceptions.ResourceNotFoundException;
import com.isyeriegitimi.backend.model.FavoriteAnnouncement;
import com.isyeriegitimi.backend.model.Student;
import com.isyeriegitimi.backend.repository.FavoriteAnnouncementRepository;
import com.isyeriegitimi.backend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FavoriteAnnouncementService {


    private FavoriteAnnouncementRepository favoriteAnnouncementRepository;
    private StudentRepository studentRepository;
    @Autowired
    public FavoriteAnnouncementService(FavoriteAnnouncementRepository favoriteAnnouncementRepository, StudentRepository studentRepository) {
        this.favoriteAnnouncementRepository = favoriteAnnouncementRepository;
        this.studentRepository = studentRepository;
    }



    public List<FavoriteAnnouncement> getFavoriteAnnouncements(UUID studentId){
        List<FavoriteAnnouncement> favorites = favoriteAnnouncementRepository.findByStudent_StudentId(studentId);
        if (favorites.isEmpty()) {
            throw new ResourceNotFoundException("FavoriteAnnouncement", "studentId", studentId.toString());
        }
        Collections.sort(favorites, new Comparator<FavoriteAnnouncement>() {
            @Override
            public int compare(FavoriteAnnouncement f1, FavoriteAnnouncement f2) {
                return f2.getAnnouncement().getStartDate().compareTo(f1.getAnnouncement().getStartDate());
            }
        });
        return favorites;
    }

    public UUID save(FavoriteAnnouncementDto favoriteAnnouncementDto){
        try{
            Student student=studentRepository.findByStudentNumber(favoriteAnnouncementDto.getStudent().getStudentNumber()).
                    orElseThrow(()-> new ResourceNotFoundException("Student","studentNo",favoriteAnnouncementDto.getStudent().getStudentNumber()));

            FavoriteAnnouncement favoriteAnnouncement=FavoriteAnnouncement
                    .builder()
                    .student(student)
                    .favoriteID(favoriteAnnouncementDto.getFavoriteId())
                    .announcement(favoriteAnnouncementDto.getAnnouncement())
                    .build();
            favoriteAnnouncementRepository.save(favoriteAnnouncement);
            return favoriteAnnouncement.getFavoriteID();
        }
        catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Error saving favorite", e);
        }
    }

    public void deleteFavoriteAnnouncement(UUID studentId, UUID announcementId) {
        try {

            FavoriteAnnouncement favoriteAnnouncement = favoriteAnnouncementRepository.findByStudent_StudentIdAndAnnouncement_AnnouncementId(studentId, announcementId)
                    .orElseThrow(() -> new RuntimeException("Favorite announcement not found"));
            favoriteAnnouncementRepository.delete(favoriteAnnouncement);
        } catch (Exception e) {
            throw new RuntimeException("Error removing favorite", e);
        }
    }


}
