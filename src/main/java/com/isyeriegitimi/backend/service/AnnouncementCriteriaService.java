package com.isyeriegitimi.backend.service;

import com.isyeriegitimi.backend.model.Announcement;
import com.isyeriegitimi.backend.model.AnnouncementCriteria;
import com.isyeriegitimi.backend.repository.AnnouncementCriteriaRepository;
import com.isyeriegitimi.backend.repository.AnnouncementRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnnouncementCriteriaService {

    private AnnouncementCriteriaRepository announcementCriteriaRepository;
    private AnnouncementRepository announcementRepository;

    public AnnouncementCriteriaService(AnnouncementCriteriaRepository announcementCriteriaRepository, AnnouncementRepository announcementRepository) {
        this.announcementCriteriaRepository = announcementCriteriaRepository;
        this.announcementRepository = announcementRepository;
    }

    public void save(List<AnnouncementCriteria> announcementCriteriaList){
        for (AnnouncementCriteria criteria : announcementCriteriaList) {
            AnnouncementCriteria newCriteria = new AnnouncementCriteria();
            newCriteria.setIlan(criteria.getIlan());
            newCriteria.setKriterAciklama(criteria.getKriterAciklama());

            announcementCriteriaRepository.save(newCriteria);
        }
    }
    public String save(List<AnnouncementCriteria> announcementCriteriaList,Long announcementId){
        Optional<Announcement> announcement=announcementRepository.findById(announcementId);
        if (announcement.isEmpty()){
            return "Announcement not found";
        }
        for (AnnouncementCriteria criteria : announcementCriteriaList) {
            AnnouncementCriteria newCriteria = new AnnouncementCriteria();
            newCriteria.setIlan(announcement.get());
            newCriteria.setKriterAciklama(criteria.getKriterAciklama());

            announcementCriteriaRepository.save(newCriteria);
        }
        return "Succesfully added.";
    }

    public List<AnnouncementCriteria> getCriteriaByAnnouncementId(Long id){

        List<AnnouncementCriteria> criteriaList= announcementCriteriaRepository.getAnnouncementCriteriaByIlanIlanId(id);
        return criteriaList;
    }

    @Transactional
    public void deleteAllCriteriasByAnnouncementId(Long announcementId) {
        announcementCriteriaRepository.deleteAllByIlan_IlanId(announcementId);
    }

    @Transactional
    public String deleteCriterias( Long criteriaId) {
        announcementCriteriaRepository.deleteByKriterId(criteriaId);
        return "Successfully deleted";
    }
}
