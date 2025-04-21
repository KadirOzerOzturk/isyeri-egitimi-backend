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

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
            Student student=studentRepository.findById(skillDto.getStudentId())
                    .orElseThrow(()-> new ResourceNotFoundException("Student","student id",skillDto.getStudentId().toString()));

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

    public List<SkillDto> getSkills(UUID studentId) {
        try {
            List<Skill> skillList = skillRepository.findAllByStudent_StudentId(studentId);

            return skillList.stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred while fetching the skills: " + e.getMessage());
        }
    }


    public void deleteSkill(UUID skillId) {
        try {
            Optional<Skill> skill=skillRepository.findById(skillId);
            if(skill.isEmpty()){
                throw new ResourceNotFoundException("Skill","skillId",skillId.toString());
            }
            skillRepository.deleteById(skillId);

        }catch (Exception e){
            throw new RuntimeException("Error removing skill", e);
        }
    }
    private SkillDto mapToDto(Skill skill) {
        SkillDto skillDto = new SkillDto();
        skillDto.setSkillId(skill.getSkillId());
        skillDto.setDescription(skill.getDescription());
        skillDto.setLevel(skill.getLevel());
        skillDto.setStudentId(skill.getStudent().getStudentId());
        return skillDto;
    }
    private Skill mapToEntity(SkillDto skillDto) {

        Skill skill = new Skill();
        skill.setSkillId(skillDto.getSkillId());
        skill.setDescription(skillDto.getDescription());
        skill.setLevel(skillDto.getLevel());
        skill.setStudent(studentRepository.findById(skillDto.getStudentId()).get());
        return skill;
    }
}
