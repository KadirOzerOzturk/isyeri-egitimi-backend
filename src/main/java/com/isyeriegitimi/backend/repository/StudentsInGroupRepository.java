package com.isyeriegitimi.backend.repository;

import com.isyeriegitimi.backend.model.Student;
import com.isyeriegitimi.backend.model.StudentInGroup;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StudentsInGroupRepository extends JpaRepository<StudentInGroup, UUID> {


    @Transactional
    void deleteAllByStudentGroupGroupId(UUID groupId);
    List<StudentInGroup>  findAllByStudentGroupGroupId(UUID groupId);
    Optional<StudentInGroup> findByStudent_StudentNumber(String studentNo);
    void deleteByStudentStudentNumber(String studentNo);
    Optional<StudentInGroup> findByStudent_StudentId(UUID uuid);
}
