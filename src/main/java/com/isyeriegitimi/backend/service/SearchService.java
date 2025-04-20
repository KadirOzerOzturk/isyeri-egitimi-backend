package com.isyeriegitimi.backend.service;


import com.isyeriegitimi.backend.dto.SearchResultDto;
import com.isyeriegitimi.backend.exceptions.InternalServerErrorException;
import com.isyeriegitimi.backend.model.*;
import com.isyeriegitimi.backend.repository.*;
import com.isyeriegitimi.backend.security.enums.Role;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import javax.management.StringValueExp;
import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService {

    private final StudentRepository studentRepository;
    private final LecturerRepository lecturerRepository;
    private final CommissionRepository commissionRepository;
    private final CompanyRepository companyRepository;
    private final MentorRepository mentorRepository;

    public SearchService(StudentRepository studentRepository, LecturerRepository lecturerRepository, CommissionRepository commissionRepository, CompanyRepository companyRepository, MentorRepository mentorRepository) {
        this.studentRepository = studentRepository;
        this.lecturerRepository = lecturerRepository;
        this.commissionRepository = commissionRepository;
        this.companyRepository = companyRepository;
        this.mentorRepository = mentorRepository;
    }

    @Transactional
    public List<SearchResultDto> searchByName(String searchText) {
        try {
            List<Student> studentList = studentRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(searchText, searchText);
            List<Commission> commissionList = commissionRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(searchText, searchText);
            List<Company> companyList = companyRepository.findByNameContainingIgnoreCase(searchText);
            List<Lecturer> lecturerList = lecturerRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(searchText, searchText);
            List<Mentor> mentorList = mentorRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(searchText,searchText);
            List<SearchResultDto> searchResultDtoList = new ArrayList<>();

            for (Mentor mentor : mentorList) {
                SearchResultDto dto = new SearchResultDto();
                dto.setId(mentor.getId());
                dto.setName(mentor.getFirstName()+" "+mentor.getLastName());
                dto.setRole(String.valueOf(Role.MENTOR));
                searchResultDtoList.add(dto);
            }
            for (Student student : studentList) {
                SearchResultDto dto = new SearchResultDto();
                dto.setId(student.getStudentId());
                dto.setRole(String.valueOf(Role.STUDENT));
                dto.setName(student.getFirstName() + " " + student.getLastName());
                searchResultDtoList.add(dto);
            }

            for (Company company : companyList) {
                SearchResultDto dto = new SearchResultDto();
                dto.setId(company.getCompanyId());
                dto.setRole(String.valueOf(Role.COMPANY));
                dto.setName(company.getName());
                searchResultDtoList.add(dto);
            }

            for (Commission commission : commissionList) {
                SearchResultDto dto = new SearchResultDto();
                dto.setId(commission.getCommissionId());
                dto.setRole(String.valueOf(Role.COMMISSION));
                dto.setName(commission.getFirstName() + " " + commission.getLastName());
                searchResultDtoList.add(dto);
            }

            for (Lecturer lecturer : lecturerList) {
                SearchResultDto dto = new SearchResultDto();
                dto.setId(lecturer.getLecturerId());
                dto.setRole(String.valueOf(Role.LECTURER));
                dto.setName(lecturer.getFirstName() + " " + lecturer.getLastName());
                searchResultDtoList.add(dto);
            }

            return searchResultDtoList;

        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred while searching by: " + searchText + " Error: " + e.getMessage());
        }
    }

}
