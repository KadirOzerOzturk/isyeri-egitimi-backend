package com.isyeriegitimi.backend.repository;

import com.isyeriegitimi.backend.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SkillRepository extends JpaRepository<Skill,Long> {

    List<Skill> findAllByOgrenci_OgrenciNo(Long studentNo);

    Skill findByOgrenci_OgrenciNoAndSkillId(Long studentNo, Long skillId);
}
