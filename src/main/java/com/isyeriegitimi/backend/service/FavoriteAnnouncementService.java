package com.isyeriegitimi.backend.service;

import com.isyeriegitimi.backend.dto.FavoriteAnnouncementDto;
import com.isyeriegitimi.backend.dto.FavoriteAnnouncementRequest;
import com.isyeriegitimi.backend.exceptions.InternalServerErrorException;
import com.isyeriegitimi.backend.exceptions.ResourceNotFoundException;
import com.isyeriegitimi.backend.model.Announcement;
import com.isyeriegitimi.backend.model.FavoriteAnnouncement;
import com.isyeriegitimi.backend.model.Student;
import com.isyeriegitimi.backend.repository.AnnouncementRepository;
import com.isyeriegitimi.backend.repository.FavoriteAnnouncementRepository;
import com.isyeriegitimi.backend.repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FavoriteAnnouncementService {


    private FavoriteAnnouncementRepository favoriteAnnouncementRepository;
    private StudentRepository studentRepository;
    private AnnouncementRepository announcementRepository;

    public FavoriteAnnouncementService(FavoriteAnnouncementRepository favoriteAnnouncementRepository, StudentRepository studentRepository, AnnouncementRepository announcementRepository) {
        this.favoriteAnnouncementRepository = favoriteAnnouncementRepository;
        this.studentRepository = studentRepository;
        this.announcementRepository = announcementRepository;
    }

    public List<FavoriteAnnouncement> getFavoriteAnnouncements(UUID studentId){
       try{
           List<FavoriteAnnouncement> favorites = favoriteAnnouncementRepository.findByStudent_StudentId(studentId);
           if (favorites.isEmpty()) {
               return favorites;
           }
           Collections.sort(favorites, new Comparator<FavoriteAnnouncement>() {
               @Override
               public int compare(FavoriteAnnouncement f1, FavoriteAnnouncement f2) {
                   return f2.getAnnouncement().getStartDate().compareTo(f1.getAnnouncement().getStartDate());
               }
           });
           return favorites;
       }catch (Exception e){
           throw new InternalServerErrorException("Error fetching favorite" + e.getMessage());
       }
    }

    public UUID save(FavoriteAnnouncementRequest favoriteAnnouncementRequest){
        try{
            Student student=studentRepository.findById(favoriteAnnouncementRequest.getStudentId()).
                    orElseThrow(()-> new ResourceNotFoundException("Student","student id",favoriteAnnouncementRequest.getStudentId().toString()));
            Optional<Announcement> announcement= announcementRepository.findById(favoriteAnnouncementRequest.getAnnouncementId());
            if(announcement.isEmpty()){
                throw new ResourceNotFoundException("Announcement","id",favoriteAnnouncementRequest.getAnnouncementId().toString());
            }
            FavoriteAnnouncement favoriteAnnouncement=FavoriteAnnouncement
                    .builder()
                    .student(student)
                    .favoriteId(favoriteAnnouncementRequest.getFavoriteId())
                    .announcement(announcement.get())
                    .build();
            favoriteAnnouncementRepository.save(favoriteAnnouncement);
            return favoriteAnnouncement.getFavoriteId();
        }
        catch (Exception e){
            throw new InternalServerErrorException("Error saving favorite" + e.getMessage());
        }
    }
    @Transactional
    public void delete(UUID favoriteId) {
        try {
            Optional<FavoriteAnnouncement> favorite = favoriteAnnouncementRepository.findById(favoriteId);
            if (favorite.isEmpty()) {
                throw new ResourceNotFoundException("Favorite","id",favoriteId.toString());
            }
            favoriteAnnouncementRepository.deleteById(favoriteId);

        }catch (Exception e){
            throw new InternalServerErrorException("Error deleting favorite" + e.getMessage());
        }
    }
    @Transactional
    public void deleteByStudentIdAndAnnouncementId(UUID studentId, UUID announcementId) {
        try{
            Optional<FavoriteAnnouncement> favorite = favoriteAnnouncementRepository.findByStudent_StudentIdAndAnnouncement_AnnouncementId(studentId, announcementId);
            if (favorite.isEmpty()) {
                throw new ResourceNotFoundException("Favorite","id",favorite.get().getFavoriteId().toString());
            }
            favoriteAnnouncementRepository.deleteByStudent_StudentIdAndAnnouncement_AnnouncementId(studentId, announcementId);
        }catch (Exception e){
            throw new InternalServerErrorException("Error deleting favorite " + e.getMessage());
        }
    }
}
