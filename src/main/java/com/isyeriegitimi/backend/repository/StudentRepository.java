package com.isyeriegitimi.backend.repository;

import com.isyeriegitimi.backend.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface StudentRepository extends JpaRepository<Student, String> {

    public Optional<Student> findByStudentNumber(String studentNo);
}
