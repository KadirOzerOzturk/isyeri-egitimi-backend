package com.isyeriegitimi.backend.service;

import com.isyeriegitimi.backend.dto.AnnouncementDto;
import com.isyeriegitimi.backend.model.Announcement;
import com.isyeriegitimi.backend.model.AnnouncementCriteria;
import com.isyeriegitimi.backend.repository.AnnouncementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AnnouncementService {

    private AnnouncementRepository announcementRepository;
    private AnnouncementCriteriaService announcementCriteriaService;


    @Autowired
    public AnnouncementService(AnnouncementRepository announcementRepository, AnnouncementCriteriaService announcementCriteriaService) {
        this.announcementRepository = announcementRepository;
        this.announcementCriteriaService = announcementCriteriaService;
    }

    public List<Announcement> getAllAnnouncements() {
        List<Announcement> announcements = announcementRepository.findAll();

        // Başlangıç tarihine göre yeniden eskiye doğru sıralama işlemi
        announcements.sort(Comparator.comparing(Announcement::getBaslangic_tarihi).reversed());

        return announcements;
    }

    public List<Announcement> getAnnouncementsByCompanyId(Long companyId) {
        List<Announcement> announcements = announcementRepository.getAnnouncementByFirmaFirmaId(companyId);

        try {
            // Başlangıç tarihine göre yeniden eskiye doğru sıralama işlemi
            announcements.sort(Comparator.comparing(Announcement::getBaslangic_tarihi).reversed()); // This is where the error occurs

        }catch (Exception e){

        }
        return announcements;
    }

    public Optional<Announcement> getAnnouncementsById(Long announcementId) {
        return announcementRepository.findById(announcementId);
    }

    public void save(AnnouncementDto announcementDto) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 30);
        Date bitisTarihi = calendar.getTime();

        Announcement announcement = new Announcement();
        announcement.setAciklama(announcementDto.getAciklama());
        announcement.setBaslik(announcementDto.getBaslik());
        announcement.setFirma(announcementDto.getFirma());
        announcement.setBaslangic_tarihi(date);
        announcement.setBitis_tarihi(bitisTarihi);
        announcement.setPostBaslik(announcementDto.getPostBaslik());

        Announcement savedAnnouncement = announcementRepository.save(announcement);

        // İlan kriterlerini kaydetme
        List<AnnouncementCriteria> announcementCriteriaList = new ArrayList<>();
        for (String criteriaDescription : announcementDto.getAnnouncementCriteria()) {
            AnnouncementCriteria criteria = new AnnouncementCriteria();
            criteria.setIlan(savedAnnouncement);
            criteria.setKriterAciklama(criteriaDescription);
            announcementCriteriaList.add(criteria);
        }
        announcementCriteriaService.save(announcementCriteriaList,savedAnnouncement.getIlanId());
    }

    public void deleteAnnouncement(Long announcementId) {
        announcementCriteriaService.deleteAllCriteriasByAnnouncementId(announcementId);
        announcementRepository.deleteById(announcementId);

    }

    public void updateAnnouncement(Long announcementId, AnnouncementDto announcementDto) {
        Optional<Announcement> existingAnnouncement=announcementRepository.findById(announcementId);
        if (existingAnnouncement.isEmpty()){
            return;
        }
        Announcement announcement=new Announcement();
        announcement.setIlanId(announcementId);
        announcement.setAciklama(announcementDto.getAciklama());
        announcement.setBaslik(announcementDto.getBaslik());
        announcement.setFirma(announcementDto.getFirma());
        announcement.setBaslangic_tarihi(existingAnnouncement.get().getBaslangic_tarihi());
        announcement.setBitis_tarihi(existingAnnouncement.get().getBitis_tarihi());
        announcement.setPostBaslik(announcementDto.getPostBaslik());
        announcementRepository.save(announcement);
    }
}
