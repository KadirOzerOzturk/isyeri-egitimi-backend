package com.isyeriegitimi.backend.repository;

import com.isyeriegitimi.backend.model.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SurveyRepository extends JpaRepository<Survey, UUID> {

}
