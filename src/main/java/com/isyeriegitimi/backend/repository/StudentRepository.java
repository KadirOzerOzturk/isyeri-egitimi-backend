package com.isyeriegitimi.backend.repository;

import com.isyeriegitimi.backend.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


public interface StudentRepository extends JpaRepository<Student, UUID> {

    public Optional<Student> findByStudentNumber(String studentNo);
    Optional<Student> findByEmail(String email);
}
