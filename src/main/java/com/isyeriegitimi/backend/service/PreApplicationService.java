package com.isyeriegitimi.backend.service;

import com.isyeriegitimi.backend.exceptions.InternalServerErrorException;
import com.isyeriegitimi.backend.model.PreApplication;
import com.isyeriegitimi.backend.repository.PreApplicationRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class PreApplicationService {
    @Autowired
    private PreApplicationRepository preApplicationRepository;

    public PreApplication savePreApplication(PreApplication application) {
        try {
            return preApplicationRepository.save(application);
        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred while saving the application: " + e.getMessage());
        }
    }

    public List<PreApplication> getAllFormEntries() {
        try {
            return  preApplicationRepository.findAll();
        }catch (Exception e){
            throw new InternalServerErrorException("An error occurred while fetching the form entries: " + e.getMessage());
        }

    }
    public void writeToExcel(List<PreApplication> applications) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Ön Başvuru Formları");

        Row headerRow = sheet.createRow(0);
        String[] columns = {"Öğrenci No", "Ad Soyad", "TCK Numarası", "Eposta", "Telefon",
                "Genel Not Ortalaması", "Tercih Edilen Dönem", "Başarısız Dersler",
                "Şirket Bilgisi", "Protokol Durumu", "Zorunlu Staj Gün Sayısı",
                "Firmada Staj Yapma İsteği", "Özel Durumlar"};

        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }

        // Verileri eklemek için döngü
        int rowNum = 1;
        for (PreApplication application : applications) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(application.getStudentNumber());
            row.createCell(1).setCellValue(application.getFullName());
            row.createCell(2).setCellValue(application.getIdNumber());
            row.createCell(3).setCellValue(application.getEmail());
            row.createCell(4).setCellValue(application.getPhone());
            row.createCell(5).setCellValue(application.getGpa());
            row.createCell(6).setCellValue(application.getPreferredTerm());
            row.createCell(7).setCellValue(application.getFailedCourses());
            row.createCell(8).setCellValue(application.getCompanyInfo());
            row.createCell(9).setCellValue(application.getProtocolStatus());
            row.createCell(10).setCellValue(application.getMandatoryInternshipDays());
            row.createCell(11).setCellValue(application.getInternshipRequestAtCompany());
            row.createCell(12).setCellValue(application.getSpecialConditions());
        }

        try (FileOutputStream fileOut = new FileOutputStream("PreApplications.xlsx")) {
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

