package com.isyeriegitimi.backend.repository;

import com.isyeriegitimi.backend.model.FormAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FormAnswerRepository extends JpaRepository<FormAnswer, UUID> {

    List<FormAnswer> findByForm_Id(UUID id);
    List<FormAnswer> findByUserIdAndUserRole(UUID userId, String userRole);

}
