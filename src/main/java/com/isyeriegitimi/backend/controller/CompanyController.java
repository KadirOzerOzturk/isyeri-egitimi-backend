package com.isyeriegitimi.backend.controller;

import com.isyeriegitimi.backend.dto.CompanyDto;
import com.isyeriegitimi.backend.exceptions.ResourceNotFoundException;
import com.isyeriegitimi.backend.model.ApiResponse;
import com.isyeriegitimi.backend.model.Company;
import com.isyeriegitimi.backend.service.CompanyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@RestController
@RequestMapping("/company")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/allCompanies")
    public ResponseEntity<ApiResponse<List<Company>>> getAllCompanies() {
        List<Company> companies = companyService.getAllCompanies();
        return ResponseEntity.ok(ApiResponse.success(companies, "All companies fetched successfully"));
    }

    @GetMapping("/{companyId}")
    public ResponseEntity<ApiResponse<Company>> getCompanyById(@PathVariable UUID companyId) {
        Optional<Company> company = Optional.ofNullable(companyService.getCompanyById(companyId));
        return ResponseEntity.ok(ApiResponse.success(company.get(), "Company fetched successfully"));
    }

    @PutMapping("/update/{companyId}")
    public ResponseEntity<ApiResponse<String>> updateCompany(@RequestBody CompanyDto companyDto, @PathVariable UUID companyId) {
        try {
            Company company =new Company();
            company.setName(companyDto.getName());
            company.setAddress(companyDto.getAddress());
            company.setEmail(companyDto.getEmail());
            company.setCompanyId(companyDto.getCompanyId());
            company.setLogo(companyDto.getLogo());
            company.setAbout(companyDto.getAbout());
            company.setSector(companyDto.getSector());
            company.setCompanyNumber(companyDto.getCompanyNumber());
            companyService.update(company);
            return ResponseEntity.ok(ApiResponse.success("Company updated successfully", "Update successful"));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(ex.getMessage(), HttpStatus.NOT_FOUND.value()));
        }
    }

    @PostMapping("/saveCompany")
    public ResponseEntity<ApiResponse<?>> saveCompany(@RequestBody CompanyDto companyDto) {
        try {
            companyService.save(companyDto);
            return ResponseEntity.ok(ApiResponse.success("Successfully saved", "Company created successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    @DeleteMapping("/delete/{companyId}")
    public ResponseEntity<ApiResponse<?>> deleteCompany(@PathVariable UUID companyId) {
        try {
            companyService.delete(companyId);
            return ResponseEntity.ok(ApiResponse.success("Successfully deleted", "Company deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }
}
