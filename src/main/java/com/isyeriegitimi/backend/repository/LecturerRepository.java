package com.isyeriegitimi.backend.repository;

import com.isyeriegitimi.backend.model.Lecturer;
import com.isyeriegitimi.backend.model.StudentGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LecturerRepository extends JpaRepository<Lecturer,Long> {

    Optional<Lecturer> findByIzleyiciNo(String lecturerNo);

    Optional<Lecturer> findByIzleyiciId(Long izleyiciId);
    List<StudentGroup> findAllByIzleyiciId(Long id);



}
