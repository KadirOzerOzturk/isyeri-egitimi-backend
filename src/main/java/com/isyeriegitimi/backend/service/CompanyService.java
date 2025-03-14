package com.isyeriegitimi.backend.service;

import com.isyeriegitimi.backend.dto.CompanyDto;
import com.isyeriegitimi.backend.exceptions.InternalServerErrorException;
import com.isyeriegitimi.backend.exceptions.ResourceNotFoundException;
import com.isyeriegitimi.backend.model.Company;
import com.isyeriegitimi.backend.repository.CompanyRepository;
import com.isyeriegitimi.backend.security.dto.UserRequest;
import com.isyeriegitimi.backend.security.enums.Role;
import com.isyeriegitimi.backend.security.service.AuthenticationService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;


    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;

    }

    public List<Company> getAllCompanies() {
        try {
            List<Company> companies = companyRepository.findAll();
            if (companies.isEmpty()) {
                throw new ResourceNotFoundException("Companies", "No companies found", "404");
            }
            return companies;
        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred while fetching companies : "+e.getMessage());
        }
    }

    public Company getCompanyById(UUID companyId) {
        return companyRepository.findByCompanyId(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company", "Company ID", companyId.toString()));
    }



    public void update(Company company) {
        if (!companyRepository.existsById(company.getCompanyId())) {
            throw new ResourceNotFoundException("Company", "Company ID", company.getCompanyId().toString());
        }
        try {
            companyRepository.save(company);
        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred while updating the company: " + e.getMessage());
        }
    }

    public void delete(UUID companyId) {
        if (!companyRepository.existsById(companyId)) {
            throw new ResourceNotFoundException("Company", "Company ID", companyId.toString());
        }
        try {
            companyRepository.deleteById(companyId);
        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred while deleting the company: " + e.getMessage());
        }
    }

  public void save(CompanyDto companyDto) {
    try {
        companyRepository.save(mapToEntity(companyDto));
    } catch (Exception e) {
        throw new InternalServerErrorException("An error occurred while saving the company: " + e.getMessage());
    }
}


    public CompanyDto mapToDto(Company company) {
        if (company == null) {
            return null;
        }

        CompanyDto companyDto = new CompanyDto();
        companyDto.setCompanyId(company.getCompanyId());
        companyDto.setName(company.getName());
        companyDto.setEmail(company.getEmail());
        companyDto.setAddress(company.getAddress());
        companyDto.setSector(company.getSector());
        companyDto.setAbout(company.getAbout());

        return companyDto;
    }

    public Company mapToEntity(CompanyDto companyDto){
        Company company =new Company();
        company.setCompanyId(companyDto.getCompanyId());
        company.setName(companyDto.getName());
        company.setEmail(companyDto.getEmail());
        company.setAddress(companyDto.getAddress());
        company.setSector(companyDto.getSector());
        company.setAbout(companyDto.getAbout());
        return company;
    }

}
