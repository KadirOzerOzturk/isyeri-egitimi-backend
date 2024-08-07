package com.isyeriegitimi.backend.service;

import com.isyeriegitimi.backend.model.Announcement;
import com.isyeriegitimi.backend.model.Application;
import com.isyeriegitimi.backend.model.Student;
import com.isyeriegitimi.backend.repository.AnnouncementRepository;
import com.isyeriegitimi.backend.repository.ApplicationRepository;
import com.isyeriegitimi.backend.repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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





    public List<Application> getApplicationsByAnnouncementId(Long id){
        return applicationRepository.findAllByIlanIlanId(id);
    }


    public List<Application> getApplicationsByStudentNo(Long studentNo) {
        return applicationRepository.findAllByOgrenciOgrenciNo(studentNo);

    }

    public String saveApplication(Long studentNo, Long announcementId) {
        Optional<Student> student=studentRepository.findByOgrenciNo(studentNo);
        Optional<Announcement> announcement=announcementRepository.findById(announcementId);
        if (student.isEmpty() && announcement.isEmpty()){
            return "Not Found";
        }
        Application application=Application.builder()
                .firma(announcement.get().getFirma())
                .ilan(announcement.get())
                .ogrenci(student.get())
                .basvuruTarihi(new Date())
                .basvuruDurum("Firma onayı bekleniyor.")
                .build();
        applicationRepository.save(application);
        return "Application saved";
    }

    public String deleteApplication(Long studentNo, Long announcementId) {


        applicationRepository.deleteAllByOgrenciOgrenciNoAndAndIlanIlanId(studentNo,announcementId);
    return "Succesfully deleted.";

    }

    @Transactional
    public List<Application> getApplicationsByStudentNoAndCompanyId(Long studentNo, Long companyId) {
        try {
            return applicationRepository.findAllByOgrenciOgrenciNoAndFirmaFirmaId(studentNo,companyId);

        }
        catch (Exception e){
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    @Transactional
    public String updateApplication( String applicationStatus,Long applicationId) {
        Optional<Application> existingApplication=applicationRepository.findByBasvuruId(applicationId);
        if (existingApplication.isEmpty() ){
            return "Not Found";
        }
        Application application = existingApplication.get(); // Existing application object
        application.setBasvuruDurum(applicationStatus); // Update the application status

        applicationRepository.save(application);
        return "Application updated";
    }
}
