package com.isyeriegitimi.backend.repository;

import com.isyeriegitimi.backend.model.FormAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FormAnswerRepository extends JpaRepository<FormAnswer, UUID> {
}
