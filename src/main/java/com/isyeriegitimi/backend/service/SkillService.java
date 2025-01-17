package com.isyeriegitimi.backend.service;

import com.isyeriegitimi.backend.dto.SkillDto;
import com.isyeriegitimi.backend.exceptions.InternalServerErrorException;
import com.isyeriegitimi.backend.exceptions.ResourceNotFoundException;
import com.isyeriegitimi.backend.model.Skill;
import com.isyeriegitimi.backend.model.Student;
import com.isyeriegitimi.backend.repository.SkillRepository;
import com.isyeriegitimi.backend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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
            Student student=studentRepository.findByStudentNumber(skillDto.getStudent().getStudentNumber())
                    .orElseThrow(()-> new ResourceNotFoundException("Student","studentNumber",skillDto.getStudent().getStudentNumber()));

            Skill skill=Skill
                    .builder()
                    .student(student)
                    .skillId(skillDto.getSkillId())
                    .description(skillDto.getDescription())
                    .level(skillDto.getLevel())
                    .build();
            skillRepository.save(skill);
        }catch (Exception e){
            e.printStackTrace();
            throw new InternalServerErrorException("Error saving skill : "+ e.getMessage());
        }
    }

    public List<Skill> getSkills(String studentNo) {
        try {

            List<Skill> skillList = skillRepository.findAllByStudent_StudentNumber(studentNo);
            if (skillList.isEmpty()) {
                throw new ResourceNotFoundException("Skill", "studentNo", studentNo);
            }
            return skillList;
        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred while fetching the skills: " + e.getMessage());
        }
    }

    public void deleteSkill(String studentNo, UUID skillId) {
        try {
            Skill skill=skillRepository.findByStudent_StudentNumberAndSkillId(studentNo,skillId);
            if(skill==null){
                throw new ResourceNotFoundException("Skill","studentNo",studentNo);
            }
            skillRepository.delete(skill);

        }catch (Exception e){
            throw new RuntimeException("Error removing skill", e);
        }
    }
}
