package com.isyeriegitimi.backend.service;

import com.isyeriegitimi.backend.aws.S3Service;
import com.isyeriegitimi.backend.dto.AnnouncementDto;
import com.isyeriegitimi.backend.exceptions.InternalServerErrorException;
import com.isyeriegitimi.backend.exceptions.ResourceNotFoundException;
import com.isyeriegitimi.backend.model.Announcement;
import com.isyeriegitimi.backend.model.AnnouncementCriteria;
import com.isyeriegitimi.backend.model.ApiResponse;
import com.isyeriegitimi.backend.repository.AnnouncementRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
@Service
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final AnnouncementCriteriaService announcementCriteriaService;


    @Autowired
    public AnnouncementService(AnnouncementRepository announcementRepository, AnnouncementCriteriaService announcementCriteriaService) {
        this.announcementRepository = announcementRepository;
        this.announcementCriteriaService = announcementCriteriaService;

    }

    public List<Announcement> getAllAnnouncements() {
        try {
            List<Announcement> announcements = announcementRepository.findAll();
            announcements.sort(Comparator.comparing(Announcement::getStartDate).reversed());
            return announcements;
        } catch (Exception e) {
            throw new InternalServerErrorException("Announcements could not be fetched.");
        }
    }

    public List<Announcement> getAnnouncementsByCompanyId(UUID companyId) {
        try {
            List<Announcement> announcements = announcementRepository.getAnnouncementByCompanyCompanyId(companyId);
            if (announcements.isEmpty()) {
                throw new ResourceNotFoundException("Announcement", "companyId", companyId.toString());
            }
            announcements.sort(Comparator.comparing(Announcement::getStartDate).reversed());
            return announcements;
        } catch (Exception e) {
            throw new InternalServerErrorException("Announcements could not be fetched.");
        }
    }

    public Announcement getAnnouncementsById(UUID announcementId) {
        return announcementRepository.findById(announcementId)
                .orElseThrow(() -> new ResourceNotFoundException("Announcement", "id", announcementId.toString()));
    }

    public UUID save(AnnouncementDto announcementDto) {
        try {
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, 30);
            Date endDate = calendar.getTime();

            Announcement announcement = Announcement.builder()
                    .company(announcementDto.getCompany())
                    .startDate(date)
                    .endDate(endDate)
                    .title(announcementDto.getTitle())
                    .description(announcementDto.getDescription())
                    .postTitle(announcementDto.getPostTitle())
                    .build();

            Announcement savedAnnouncement = announcementRepository.save(announcement);
            return savedAnnouncement.getAnnouncementId();
        } catch (Exception e) {
            throw new InternalServerErrorException("Announcement could not be saved.");
        }
    }

    public void deleteAnnouncement(UUID announcementId) {
        try {
            announcementCriteriaService.deleteAllCriteriasByAnnouncementId(announcementId);
            announcementRepository.deleteById(announcementId);
        } catch (Exception e) {
            throw new InternalServerErrorException("Announcement could not be deleted.");
        }
    }

    public void updateAnnouncement(UUID announcementId, AnnouncementDto announcementDto) {
        Optional<Announcement> existingAnnouncement = announcementRepository.findById(announcementId);
        if (existingAnnouncement.isEmpty()) {
            throw new ResourceNotFoundException("Announcement", "id", announcementId.toString());
        }
        try {
            Announcement announcement = existingAnnouncement.get();
            announcement.setCompany(announcementDto.getCompany());
            announcement.setTitle(announcementDto.getTitle());
            announcement.setDescription(announcementDto.getDescription());
            announcement.setPostTitle(announcementDto.getPostTitle());
            announcement.setStartDate(announcementDto.getStartDate());
            announcement.setEndDate(announcementDto.getEndDate());
            announcementRepository.save(announcement);
        } catch (Exception e) {
            throw new InternalServerErrorException("Announcement could not be updated.");
        }
    }
}

