package com.isyeriegitimi.backend.repository;

import com.isyeriegitimi.backend.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company,Long> {
    Optional<Company> findByFirmaId(Long firmaId);
    Optional<Company> findByFirmaNo(String firmaNo);
}
