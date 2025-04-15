package com.isyeriegitimi.backend.service;

import com.isyeriegitimi.backend.exceptions.InternalServerErrorException;
import com.isyeriegitimi.backend.exceptions.ResourceNotFoundException;
import com.isyeriegitimi.backend.model.Announcement;
import com.isyeriegitimi.backend.model.AnnouncementCriteria;
import com.isyeriegitimi.backend.model.ApiResponse;
import com.isyeriegitimi.backend.repository.AnnouncementCriteriaRepository;
import com.isyeriegitimi.backend.repository.AnnouncementRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Service
public class AnnouncementCriteriaService {

    private AnnouncementCriteriaRepository announcementCriteriaRepository;
    private AnnouncementRepository announcementRepository;

    public AnnouncementCriteriaService(AnnouncementCriteriaRepository announcementCriteriaRepository, AnnouncementRepository announcementRepository) {
        this.announcementCriteriaRepository = announcementCriteriaRepository;
        this.announcementRepository = announcementRepository;
    }

    public void save(List<AnnouncementCriteria> announcementCriteriaList, UUID announcementId) {
        try {
            Optional<Announcement> announcement = announcementRepository.findById(announcementId);
            if (announcement.isEmpty()) {
                throw new ResourceNotFoundException("Announcement", "id", announcementId.toString());
            }
            for (AnnouncementCriteria criteria : announcementCriteriaList) {
                AnnouncementCriteria newCriteria = new AnnouncementCriteria();
                newCriteria.setAnnouncement(announcement.get());
                newCriteria.setCriteriaDescription(criteria.getCriteriaDescription());
                announcementCriteriaRepository.save(newCriteria);
            }
        }catch (Exception e){
            throw new InternalServerErrorException("Failed to save criteria: " + e.getMessage());
        }
    }

    public List<AnnouncementCriteria> getCriteriaByAnnouncementId(UUID id) {
        try {
            List<AnnouncementCriteria> criteriaList = announcementCriteriaRepository.getAnnouncementCriteriaByAnnouncementAnnouncementId(id);

            return criteriaList;
        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to get criterias: " + e.getMessage());
        }
    }

    @Transactional
    public void deleteCriterias(UUID criteriaId) {
        try {
            announcementCriteriaRepository.deleteAllByCriteriaId(criteriaId);
        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to delete criteria: " + e.getMessage());
        }
    }

    @Transactional
    public void deleteAllCriteriasByAnnouncementId(UUID announcementId) {
        try {
            announcementCriteriaRepository.deleteAllByAnnouncement_AnnouncementId(announcementId);
        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to delete all criteria: " + e.getMessage());
        }
    }

    public void bulkSave(List<AnnouncementCriteria> announcementCriteria, UUID announcementId) {
        try {
            Optional<Announcement> announcement = announcementRepository.findById(announcementId);
            if (announcement.isEmpty()) {
                throw new ResourceNotFoundException("Announcement", "id", announcementId.toString());
            }
            for (AnnouncementCriteria criteria : announcementCriteria) {
                AnnouncementCriteria newCriteria = new AnnouncementCriteria();
                newCriteria.setAnnouncement(announcement.get());
                newCriteria.setCriteriaDescription(criteria.getCriteriaDescription());
                announcementCriteriaRepository.save(newCriteria);
            }
        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to save criteria: " + e.getMessage());
        }
    }
}

