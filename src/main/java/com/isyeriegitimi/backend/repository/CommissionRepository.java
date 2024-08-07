package com.isyeriegitimi.backend.repository;

import com.isyeriegitimi.backend.model.Commission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommissionRepository extends JpaRepository<Commission,Long> {
    Optional<Commission> findByKomisyonNo(String komisyonNo);
}
