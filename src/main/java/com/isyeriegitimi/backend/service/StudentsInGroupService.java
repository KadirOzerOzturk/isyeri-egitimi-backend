package com.isyeriegitimi.backend.service;

import com.isyeriegitimi.backend.dto.StudentInGroupDto;
import com.isyeriegitimi.backend.model.Lecturer;
import com.isyeriegitimi.backend.model.Student;
import com.isyeriegitimi.backend.model.StudentGroup;
import com.isyeriegitimi.backend.model.StudentInGroup;
import com.isyeriegitimi.backend.repository.LecturerRepository;
import com.isyeriegitimi.backend.repository.StudentGroupRepository;
import com.isyeriegitimi.backend.repository.StudentRepository;
import com.isyeriegitimi.backend.repository.StudentsInGroupRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
public class StudentsInGroupService {

    private final StudentsInGroupRepository studentsInGroupRepository;
    private final LecturerRepository  lecturerRepository;
    private final StudentRepository studentRepository;

    private final StudentGroupRepository studentGroupRepository;

    @Autowired
    public StudentsInGroupService(StudentsInGroupRepository studentsInGroupRepository, LecturerRepository lecturerRepository, StudentRepository studentRepository, StudentGroupRepository studentGroupRepository) {
        this.studentsInGroupRepository = studentsInGroupRepository;
        this.lecturerRepository = lecturerRepository;
        this.studentRepository = studentRepository;
        this.studentGroupRepository = studentGroupRepository;
    }

    public List<StudentInGroup> getStudentsByGroupId(int groupId) {
        return studentsInGroupRepository.findAllByStudentGroupGrupId(groupId);
    }

    public List<StudentInGroup> findAll() {
        return studentsInGroupRepository.findAll();
    }


    public String addStudentToGroup(Long groupId, Long studentNo) {
        Optional<Student>  student=studentRepository.findByOgrenciNo(studentNo);
        Optional<StudentGroup>  studentGroup=studentGroupRepository.findById(groupId);
        if (student.isEmpty()|| studentGroup.isEmpty()){
            return "Student or student group not found";
        }
        StudentInGroup studentInGroup = new StudentInGroup();
        studentInGroup.setStudent(student.get());
        studentInGroup.setStudentGroup(studentGroup.get());
        studentsInGroupRepository.save(studentInGroup);
        return "Successfully added";
    }

    private StudentInGroupDto mapToDto(StudentInGroup studentInGroup){
        StudentInGroupDto  studentInGroupDto=new StudentInGroupDto();
        studentInGroupDto.setStudentGroup(studentInGroup.getStudentGroup());
        studentInGroupDto.setId(studentInGroup.getId());
        studentInGroupDto.setStudent(studentInGroup.getStudent());
        return  studentInGroupDto;
    }
    private StudentInGroup mapToDto(StudentInGroupDto studentInGroupDto){
        StudentInGroup  studentInGroup=new StudentInGroup();
        studentInGroup.setStudentGroup(studentInGroupDto.getStudentGroup());
        studentInGroup.setId(studentInGroupDto.getId());
        studentInGroup.setStudent(studentInGroupDto.getStudent());
        return  studentInGroup;
    }

    @Transactional
    public String deleteStudent( Long studentNo) {
        studentsInGroupRepository.deleteByStudentOgrenciNo(studentNo);
        return "Student successfully deleted from group ";
    }
}
