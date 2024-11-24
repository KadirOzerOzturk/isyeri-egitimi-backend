package com.isyeriegitimi.backend.service;

import com.isyeriegitimi.backend.dto.CompanyDto;
import com.isyeriegitimi.backend.model.Company;
import com.isyeriegitimi.backend.repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public List<Company> getAllCompanies(){
        return companyRepository.findAll();
    }

    public Optional<Company> getCompanyById(Long companyId) {
        return companyRepository.findByFirmaId(companyId);
    }

    public Optional<Company> getCompanyByCompanyNo(String firmaNo) {
        return companyRepository.findByFirmaNo(firmaNo);
    }

    public void update(Company company) {
        Optional<Company> exitingStudent=companyRepository.findByFirmaId(company.getFirmaId());
        if (exitingStudent.isPresent()){
            companyRepository.save(company);
        }
    }

    public void save(CompanyDto companyDto) {
        companyRepository.save(mapToEntity(companyDto));

    }

    private Company mapToEntity(CompanyDto companyDto){
        Company company =new Company();
        company.setFirmaId(Long.parseLong(companyDto.getFirmaNo()));
        company.setFirmaAd(companyDto.getFirmaAd());
        company.setFirmaAdres(companyDto.getFirmaAdres());
        company.setFirmaEposta(companyDto.getFirmaEposta());
        company.setFirmaLogo(companyDto.getFirmaLogo());
        company.setFirmaHakkinda(companyDto.getFirmaHakkinda());
        company.setFirmaParola(companyDto.getFirmaParola());
        company.setFirmaSektor(companyDto.getFirmaSektor());
        company.setFirmaNo(companyDto.getFirmaNo());
        return company;
    }

    public void delete(Long companyId) {
        companyRepository.deleteById(companyId);
    }
}
