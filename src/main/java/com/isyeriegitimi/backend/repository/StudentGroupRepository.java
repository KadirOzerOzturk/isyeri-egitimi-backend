package com.isyeriegitimi.backend.repository;

import com.isyeriegitimi.backend.model.StudentGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentGroupRepository extends JpaRepository<StudentGroup,Long> {
    List<StudentGroup> findAllByIzleyiciIzleyiciId(Long id);



}
