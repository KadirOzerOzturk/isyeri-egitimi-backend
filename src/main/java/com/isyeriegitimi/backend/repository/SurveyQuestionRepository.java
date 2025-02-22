package com.isyeriegitimi.backend.repository;

import com.isyeriegitimi.backend.model.SurveyQuestion;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SurveyQuestionRepository extends JpaRepository<SurveyQuestion, UUID> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO survey_question (survey_id, options, question_number, question_text,question_type, question_id) " +
            "VALUES (:surveyId, CAST(:options AS jsonb), :questionNumber, :questionText,:questionType, :questionId)",
            nativeQuery = true)
    void insertQuestion(@Param("surveyId") UUID surveyId,
                        @Param("options") String options, // options doğrudan JSON string olmalı
                        @Param("questionNumber") int questionNumber,
                        @Param("questionText") String questionText,
                        @Param("questionType") String questionType,
                        @Param("questionId") UUID questionId);
    @Modifying
    @Transactional
    @Query(value = "UPDATE survey_question SET options = CAST(:options AS jsonb), question_number = :questionNumber, question_text = :questionText, question_type = :questionType WHERE question_id = :questionId",
            nativeQuery = true)
    void updateQuestion(@Param("options") String options,
                        @Param("questionNumber") int questionNumber,
                        @Param("questionText") String questionText,
                        @Param("questionType") String questionType,
                        @Param("questionId") UUID questionId);


    List<SurveyQuestion> findBySurvey_Id(UUID surveyId);
    void deleteAllBySurveyId(UUID surveyId);
}
