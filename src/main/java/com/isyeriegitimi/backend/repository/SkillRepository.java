package com.isyeriegitimi.backend.repository;

import com.isyeriegitimi.backend.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SkillRepository extends JpaRepository<Skill, UUID> {

    List<Skill> findAllByStudent_StudentNumber(String studentNo);

    Skill findByStudent_StudentNumberAndSkillId(String studentNo, UUID skillId);
}
