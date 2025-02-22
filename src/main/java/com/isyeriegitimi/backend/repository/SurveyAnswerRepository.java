package com.isyeriegitimi.backend.repository;

import com.isyeriegitimi.backend.model.SurveyAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SurveyAnswerRepository extends JpaRepository<SurveyAnswer, UUID> {

    List<SurveyAnswer> findBySurveyIdAndUserId(UUID surveyId, UUID userId);
    Optional<SurveyAnswer> findBySurveyQuestion_QuestionId(UUID questionId);
}
