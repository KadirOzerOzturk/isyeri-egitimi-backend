package com.isyeriegitimi.backend.service;

import com.isyeriegitimi.backend.dto.AnnouncementDto;
import com.isyeriegitimi.backend.exceptions.InternalServerErrorException;
import com.isyeriegitimi.backend.exceptions.ResourceNotFoundException;
import com.isyeriegitimi.backend.model.Announcement;
import com.isyeriegitimi.backend.model.AnnouncementCriteria;
import com.isyeriegitimi.backend.model.FavoriteAnnouncement;
import com.isyeriegitimi.backend.repository.AnnouncementRepository;
import com.isyeriegitimi.backend.repository.FavoriteAnnouncementRepository;
import com.isyeriegitimi.backend.repository.FileInfoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final AnnouncementCriteriaService announcementCriteriaService;
    private final FavoriteAnnouncementRepository favoriteAnnouncementRepository;
    private final FileInfoRepository fileInfoRepository;

    public AnnouncementService(AnnouncementRepository announcementRepository, AnnouncementCriteriaService announcementCriteriaService, FavoriteAnnouncementRepository favoriteAnnouncementRepository,FileInfoRepository fileInfoRepository) {
        this.announcementRepository = announcementRepository;
        this.announcementCriteriaService = announcementCriteriaService;
        this.favoriteAnnouncementRepository = favoriteAnnouncementRepository;
        this.fileInfoRepository = fileInfoRepository;
    }

    public List<Announcement> getAllAnnouncements() {
        try {
            List<Announcement> announcements = announcementRepository.findAll();
            announcements.sort(Comparator.comparing(Announcement::getStartDate).reversed());
            return announcements;
        } catch (Exception e) {
            throw new InternalServerErrorException("Announcements could not be fetched. "+e.getMessage());
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
            throw new InternalServerErrorException("Announcements could not be fetched."+e.getMessage());
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
            if (announcementDto.getAnnouncementCriteria() != null) {
                List<AnnouncementCriteria> announcementCriteriaList = new ArrayList<>();
                for (String criteria : announcementDto.getAnnouncementCriteria()) {
                    AnnouncementCriteria announcementCriteria = new AnnouncementCriteria();
                    announcementCriteria.setAnnouncement(savedAnnouncement);
                    announcementCriteria.setCriteriaDescription(criteria);
                    announcementCriteriaList.add(announcementCriteria);
                }
                announcementCriteriaService.bulkSave(announcementCriteriaList, savedAnnouncement.getAnnouncementId());
            }
            return savedAnnouncement.getAnnouncementId();
        } catch (Exception e) {
            throw new InternalServerErrorException("Announcement could not be saved. "+e.getMessage());
        }
    }
    @Transactional
    public void deleteAnnouncement(UUID announcementId) {
        try {
            announcementCriteriaService.deleteAllCriteriasByAnnouncementId(announcementId);
            Optional<FavoriteAnnouncement> favoriteAnnouncement= favoriteAnnouncementRepository.findByAnnouncement_AnnouncementId(announcementId);
            if (favoriteAnnouncement.isPresent()) {
                favoriteAnnouncementRepository.deleteAllByAnnouncement_AnnouncementId(favoriteAnnouncement.get().getAnnouncement().getAnnouncementId());
            }
            fileInfoRepository.deleteByOwnerId(String.valueOf(announcementId));
            announcementRepository.deleteById(announcementId);
        } catch (Exception e) {
            throw new InternalServerErrorException("Announcement could not be deleted." +e.getMessage());
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
            throw new InternalServerErrorException("Announcement could not be updated." +e.getMessage());
        }
    }
}

