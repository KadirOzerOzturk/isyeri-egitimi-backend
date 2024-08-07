package com.isyeriegitimi.backend.repository;

import com.isyeriegitimi.backend.model.StudentInGroup;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentsInGroupRepository extends JpaRepository<StudentInGroup,Integer> {

    List<StudentInGroup>  findAllByStudentGroupGrupId(int groupId);
    Optional<StudentInGroup> findByStudentOgrenciNo(Long studentNo);
    void deleteByStudentOgrenciNo(Long studentNo);
    @Transactional
    void deleteAllByStudentGroupGrupId(Long groupId);
}
