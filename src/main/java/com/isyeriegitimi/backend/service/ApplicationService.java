package com.isyeriegitimi.backend.service;

import com.isyeriegitimi.backend.exceptions.InternalServerErrorException;
import com.isyeriegitimi.backend.exceptions.ResourceNotFoundException;
import com.isyeriegitimi.backend.model.Announcement;
import com.isyeriegitimi.backend.model.Application;
import com.isyeriegitimi.backend.model.Student;
import com.isyeriegitimi.backend.repository.AnnouncementRepository;
import com.isyeriegitimi.backend.repository.ApplicationRepository;
import com.isyeriegitimi.backend.repository.StudentRepository;
import com.isyeriegitimi.backend.security.enums.Role;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ApplicationService {

    private ApplicationRepository applicationRepository;
    private StudentRepository studentRepository;
    private AnnouncementRepository announcementRepository;

    @Autowired
    public ApplicationService(ApplicationRepository applicationRepository, StudentRepository studentRepository, AnnouncementRepository announcementRepository) {
        this.applicationRepository = applicationRepository;
        this.studentRepository = studentRepository;
        this.announcementRepository = announcementRepository;
    }

    public List<Application> getApplicationsByAnnouncementId(UUID id){
        try {
            List<Application> applications = applicationRepository.findAllByAnnouncement_AnnouncementId(id);

            return applications;
        }
        catch (Exception e) {
            throw new InternalServerErrorException("Applications could not be fetched.");
        }
    }


    public List<Application> getApplicationsByStudentId(UUID studentId) {
        try {
            List<Application> applications = applicationRepository.findAllByStudent_StudentId(studentId);

            return applications;
        }catch (Exception e){
            throw new InternalServerErrorException("Applications could not be fetched : " + e.getMessage());
        }
    }

   public void saveApplication(UUID studentId, UUID announcementId) {
    try {
        Optional<Student> student = studentRepository.findById(studentId);
        Optional<Announcement> announcement = announcementRepository.findById(announcementId);
        if (student.isEmpty() && announcement.isEmpty()) {
        }
        Application application = Application.builder()
                .company(announcement.get().getCompany())
                .announcement(announcement.get())
                .student(student.get())
                .applicationDate(new Date())
                .pendingRole(String.valueOf(Role.COMPANY))
                .applicationStatus("Firma onayı bekleniyor.")
                .build();
        applicationRepository.save(application);
    } catch (Exception e) {
        throw new InternalServerErrorException("Application could not be saved.");
    }
}

    public void deleteApplication(String studentNo, UUID announcementId) {
        try {
            applicationRepository.deleteAllByStudentStudentNumberAndAnnouncementAnnouncementId(studentNo,announcementId);
        }catch (Exception e){
            throw new InternalServerErrorException("Application could not be deleted.");
        }
    }

    @Transactional
    public List<Application> getApplicationsByStudentIdAndCompanyId(UUID studentId, UUID companyId) {
        try {
            List<Application> applications = applicationRepository.findAllByStudent_StudentIdAndCompanyCompanyId(studentId, companyId);

            return applications;
        }
        catch (Exception e){
            return null;
        }
    }
    @Transactional
    public void updateApplication(String pendingRole, UUID applicationId) {
    try {
        Optional<Application> existingApplication = applicationRepository.findByApplicationId(applicationId);
        if (existingApplication.isEmpty()) {
            throw new ResourceNotFoundException("Application", "id", applicationId.toString());
        }
        Application application = existingApplication.get();
        application.setPendingRole(pendingRole);
        if (pendingRole.equals(String.valueOf(Role.COMPANY))) {
            application.setApplicationStatus("Firma onayı bekleniyor.");
        } else if (pendingRole.equals(String.valueOf(Role.STUDENT))) {
            application.setApplicationStatus("Öğrenci onayı bekleniyor.");
        } else if (pendingRole.equals(String.valueOf(Role.COMMISSION))) {
            application.setApplicationStatus("Komisyon onayı bekleniyor.");
        }else if (pendingRole.equals("DONE")) {
            application.setApplicationStatus("Onaylandı.");
        }
        applicationRepository.save(application);

    } catch (Exception e) {
        throw new InternalServerErrorException("Application could not be updated.");
    }
}

    public List<Application> getPendingApplicationsByRole(String confirmingRole) {
        try{
            List<Application> applications = applicationRepository.findByPendingRole(confirmingRole);
            return applications;
        }catch (Exception e){
            throw  new InternalServerErrorException("Applications could not be fetched : " + e.getMessage());
        }
    }

    public List<Application> getAllApplications() {
        try {
            List<Application> applications = applicationRepository.findAll();
            return applications;

        }
        catch (Exception e){
            throw new InternalServerErrorException("Applications could not be fetched : " + e.getMessage());
        }
    }
}
