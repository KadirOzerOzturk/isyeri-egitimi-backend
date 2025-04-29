package com.isyeriegitimi.backend.repository;

import com.isyeriegitimi.backend.model.FormQuestion;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FormQuestionRepository extends JpaRepository<FormQuestion, UUID> {
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO form_question (form_id, options, question_number, question_text, question_id,question_type,required_for) " +
            "VALUES (:formId, CAST(:options AS jsonb), :questionNumber, :questionText, :questionId,:questionType,:requiredFor)",
            nativeQuery = true)
    void insertQuestion(@Param("formId") UUID formId,
                        @Param("options") String options,
                        @Param("questionNumber") int questionNumber,
                        @Param("questionText") String questionText,
                        @Param("questionId") UUID questionId,
                        @Param("questionType") String questionType,
                        @Param("requiredFor") String requiredFor);
    @Query(value = "SELECT * FROM form_question WHERE question_id = :questionId", nativeQuery = true)
    FormQuestion getQuestionByQuestionId(@Param("questionId") UUID questionId);


    List<FormQuestion> findByForm_Id(UUID formId);

    void deleteAllByFormId(UUID id);
}
