package com.isyeriegitimi.backend.repository;

import com.isyeriegitimi.backend.model.Commission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CommissionRepository extends JpaRepository<Commission, UUID> {
    Optional<Commission> findByCommissionNumber(String commissionNumber);
}
