package com.isyeriegitimi.backend.controller;


import com.isyeriegitimi.backend.dto.*;
import com.isyeriegitimi.backend.model.Commission;
import com.isyeriegitimi.backend.model.Company;
import com.isyeriegitimi.backend.model.Lecturer;
import com.isyeriegitimi.backend.model.Student;
import com.isyeriegitimi.backend.service.CommissionService;
import com.isyeriegitimi.backend.service.CompanyService;
import com.isyeriegitimi.backend.service.LecturerService;
import com.isyeriegitimi.backend.service.StudentService;
import com.isyeriegitimi.backend.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final StudentService studentService;
    private final CompanyService companyService;
    private final LecturerService lecturerService;
    private final CommissionService commissionService;

    @Autowired
    public AuthController(StudentService studentService, CompanyService companyService, LecturerService lecturerService, CommissionService commissionService) {
        this.studentService = studentService;
        this.companyService = companyService;
        this.lecturerService = lecturerService;
        this.commissionService = commissionService;
    }

    @PostMapping("/student-login")
    public ResponseEntity<?> studentLogin(@RequestBody StudentLoginDto studentLoginDto){
        Optional<Student> existingStudent = studentService.getStudentByStudentNo(studentLoginDto.getOgrenciNo());

        if (existingStudent.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        Student student = existingStudent.get();

        if (studentLoginDto.getParola().equals(student.getOgrenciParola())){
            StudentDto studentDto = new StudentDto();
            studentDto.setOgrenciNo(student.getOgrenciNo());
            studentDto.setOgrenciAd(student.getOgrenciAd());
            studentDto.setOgrenciSoyad(student.getOgrenciSoyad());
            studentDto.setOgrenciEposta(student.getOgrenciEposta());
            studentDto.setOgrenciTelNo(student.getOgrenciTelNo());
            studentDto.setOgrenciKimlikNo(student.getOgrenciKimlikNo());
            studentDto.setOgrenciAdres(student.getOgrenciAdres());
            studentDto.setOgrenciAgno(student.getOgrenciAgno());
            studentDto.setOgrenciParola(student.getOgrenciParola());
            studentDto.setOgrenciSinif(student.getOgrenciSinif());
            studentDto.setOgrenciFotograf(student.getOgrenciFotograf());
            studentDto.setOgrenciFakulte(student.getOgrenciFakulte());
            studentDto.setOgrenciHakkinda(student.getOgrenciHakkinda());
            studentDto.setFirma(student.getFirma());

            return ResponseEntity.ok(studentDto);
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password is not correct");
        }
    }

    @PostMapping("/company-login")
    public ResponseEntity<?> companyLogin(@RequestBody CompanyLoginDto companyLoginDto){
        Optional<Company> existingCompany = companyService.getCompanyById(companyLoginDto.getFirmaId());

        if (existingCompany.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Company not found");
        }

        Company company = existingCompany.get();

        if (companyLoginDto.getFirmaParola().equals(company.getFirmaParola())){
            CompanyDto companyDto = new CompanyDto();
            companyDto.setFirmaAd(company.getFirmaAd());
            companyDto.setFirmaAdres(company.getFirmaAdres());
            companyDto.setFirmaEposta(company.getFirmaEposta());
            companyDto.setFirmaId(company.getFirmaId());
            companyDto.setFirmaLogo(company.getFirmaLogo());
            companyDto.setFirmaHakkinda(company.getFirmaHakkinda());
            companyDto.setFirmaNo(company.getFirmaNo());
            companyDto.setFirmaSektor(company.getFirmaSektor());
            companyDto.setFirmaParola(company.getFirmaParola());
            return ResponseEntity.ok(companyDto);
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password is not correct");
        }
    }
    @PostMapping("/lecturer-login")
    public ResponseEntity<?> lecturerLogin(@RequestBody LecturerLoginDto lecturerLoginDto){
       Optional<Lecturer> existingLecturer =lecturerService.getLecturerByLecturerNo(lecturerLoginDto.getIzleyiciNo());
       if (existingLecturer.isEmpty()){
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lecturer not found");
       }

       Lecturer lecturer =existingLecturer.get();
       if (lecturer.getIzleyiciParola().equals(lecturerLoginDto.getIzleyiciParola())){
           LecturerDto lecturerDto=new LecturerDto();
           lecturerDto.setIzleyiciId(lecturer.getIzleyiciId());
           lecturerDto.setIzleyiciAd(lecturer.getIzleyiciAd());
           lecturerDto.setIzleyiciSoyad(lecturer.getIzleyiciSoyad());
           lecturerDto.setIzleyiciEposta(lecturer.getIzleyiciEposta());
           lecturerDto.setIzleyiciParola(lecturer.getIzleyiciParola());
           lecturerDto.setIzleyiciNo(lecturer.getIzleyiciNo());
           lecturerDto.setIzleyiciFakulte(lecturer.getIzleyiciFakulte());
           lecturerDto.setIzleyiciHakkinda(lecturer.getIzleyiciHakkinda());
           return  ResponseEntity.ok(lecturerDto);

       }
       else {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password is not correct");
       }
    }
    @PostMapping("/commission-login")
    public ResponseEntity<?> commissionLogin(@RequestBody CommissionLoginDto commissionLoginDto){
        Optional<Commission> existingCommission=commissionService.getCommissionByCommissionNo(commissionLoginDto.getKomisyonNo());
        if (existingCommission.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Commission no not found");

        }
        if (existingCommission.get().getKomisyonParola().equals(commissionLoginDto.getKomisyonParola())){
            CommissionDto commissionDto=new CommissionDto();
            commissionDto.setKomisyonAd(existingCommission.get().getKomisyonAd());
            commissionDto.setKomisyonId(existingCommission.get().getKomisyonId());
            commissionDto.setKomisyonEposta(existingCommission.get().getKomisyonEposta());
            commissionDto.setKomisyonNo(existingCommission.get().getKomisyonNo());
            commissionDto.setKomisyonSoyad(existingCommission.get().getKomisyonSoyad());
            commissionDto.setKomisyonParola(existingCommission.get().getKomisyonParola());
            return ResponseEntity.ok(commissionDto);
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password is not correct");
        }
    }

}
