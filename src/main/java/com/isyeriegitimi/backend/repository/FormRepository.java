package com.isyeriegitimi.backend.repository;

import com.isyeriegitimi.backend.model.Form;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FormRepository extends JpaRepository<Form, UUID> {
}
