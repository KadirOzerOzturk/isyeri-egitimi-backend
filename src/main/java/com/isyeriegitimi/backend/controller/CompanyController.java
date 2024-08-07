package com.isyeriegitimi.backend.controller;

import com.isyeriegitimi.backend.dto.CompanyDto;
import com.isyeriegitimi.backend.model.Company;
import com.isyeriegitimi.backend.service.CompanyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/company")
public class CompanyController {

    private final CompanyService  companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/allCompanies")
    public ResponseEntity<List<Company>>  getAllCompanies(){
        return ResponseEntity.ok(companyService.getAllCompanies());
    }
    @GetMapping("/{companyId}")
    public Optional<Company> getCompanyById(@PathVariable Long companyId){

        return companyService.getCompanyById(companyId);
    }
    @PutMapping("/update/{companyId}")
    public ResponseEntity<String> updateCompany(@RequestBody CompanyDto companyDto, @PathVariable Long companyId) {
        Optional<Company> existingCompany=companyService.getCompanyById(companyId);
        if (existingCompany.isPresent()) {
            Company company =new Company();
            company.setFirmaAd(companyDto.getFirmaAd());
            company.setFirmaAdres(companyDto.getFirmaAdres());
            company.setFirmaEposta(companyDto.getFirmaEposta());
            company.setFirmaId(companyDto.getFirmaId());
            company.setFirmaLogo(companyDto.getFirmaLogo());
            company.setFirmaHakkinda(companyDto.getFirmaHakkinda());
            company.setFirmaParola(companyDto.getFirmaParola());
            company.setFirmaSektor(companyDto.getFirmaSektor());
            company.setFirmaNo(companyDto.getFirmaNo());
            companyService.update(company);
            return ResponseEntity.ok("Successfully updated");

        }else {
            return ResponseEntity.notFound().build();
        }

    }
    @PostMapping("/saveCompany")
    public ResponseEntity<?> saveCompany(@RequestBody CompanyDto  companyDto){
        try {
            companyService.save(companyDto);
            return ResponseEntity.ok().body("Successfully saved");
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @DeleteMapping("/delete/{companyId}")
    public ResponseEntity<?>  deleteCompany(@PathVariable Long  companyId){
        try {
            companyService.delete(companyId);
            return ResponseEntity.ok().body("Successfully deleted");
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    }
