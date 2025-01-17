package com.isyeriegitimi.backend.repository;

import com.isyeriegitimi.backend.model.StudentGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StudentGroupRepository extends JpaRepository<StudentGroup,UUID> {
    List<StudentGroup> findAllByLecturerLecturerId(UUID id);



}
