package com.isyeriegitimi.backend.service;

import com.isyeriegitimi.backend.dto.SkillDto;
import com.isyeriegitimi.backend.model.Skill;
import com.isyeriegitimi.backend.model.Student;
import com.isyeriegitimi.backend.repository.SkillRepository;
import com.isyeriegitimi.backend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillService {

    private SkillRepository skillRepository;
    private StudentRepository studentRepository;
    @Autowired
    public SkillService(SkillRepository skillRepository, StudentRepository studentRepository) {
        this.skillRepository = skillRepository;
        this.studentRepository = studentRepository;
    }




    public void saveSkill(SkillDto skillDto){
        try {
            Student student=studentRepository.findByOgrenciNo(skillDto.getOgrenci().getOgrenciNo()).orElseThrow(()-> new RuntimeException("User Not Found"));

            Skill skill=Skill
                    .builder()
                    .ogrenci(student)
                    .skillId(skillDto.getSkillId())
                    .aciklama(skillDto.getAciklama())
                    .seviye(skillDto.getSeviye())
                    .build();
            skillRepository.save(skill);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Error saving skill", e);
        }
    }

    public List<Skill> getSkills(Long studentNo) {

        return skillRepository.findAllByOgrenci_OgrenciNo(studentNo);
    }

    public void deleteSkill(Long studentNo, Long skillId) {
        try {
            Skill skill=skillRepository.findByOgrenci_OgrenciNoAndSkillId(studentNo,skillId);
            skillRepository.delete(skill);

        }catch (Exception e){
            throw new RuntimeException("Error removing skill", e);
        }
    }
}
