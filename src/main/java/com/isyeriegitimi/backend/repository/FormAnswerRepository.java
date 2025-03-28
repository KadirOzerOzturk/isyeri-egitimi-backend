package com.isyeriegitimi.backend.repository;

import com.isyeriegitimi.backend.model.FormAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FormAnswerRepository extends JpaRepository<FormAnswer, UUID> {

    List<FormAnswer> findByForm_Id(UUID id);
    List<FormAnswer> findByUserIdAndUserRole(UUID userId, String userRole);
    List<FormAnswer> findByStudentIdAndUserRoleAndForm_Id(UUID studentId, String userRole, UUID formId);
    List<FormAnswer> findByStudentIdAndFormId(UUID studentId,UUID formId);
    void deleteAllByFormId(UUID id);

    List<FormAnswer> findByForm_IdAndStudentId(UUID formId, UUID studentId);

    Optional<FormAnswer> findByForm_IdAndFormQuestionQuestionIdAndUserId(UUID formId, UUID questionId, UUID userId);
}
