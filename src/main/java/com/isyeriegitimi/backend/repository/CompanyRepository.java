package com.isyeriegitimi.backend.repository;

import com.isyeriegitimi.backend.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company,UUID> {
    Optional<Company> findByCompanyId(UUID companyId);
    Optional<Company> findByEmail(String email);
}
