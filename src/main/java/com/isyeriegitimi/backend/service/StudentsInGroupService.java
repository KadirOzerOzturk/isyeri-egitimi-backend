package com.isyeriegitimi.backend.service;

import com.isyeriegitimi.backend.dto.StudentInGroupDto;
import com.isyeriegitimi.backend.exceptions.InternalServerErrorException;
import com.isyeriegitimi.backend.exceptions.ResourceNotFoundException;
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

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StudentsInGroupService {

    private final StudentsInGroupRepository studentsInGroupRepository;
    private final StudentRepository studentRepository;
    private final StudentGroupRepository studentGroupRepository;

    @Autowired
    public StudentsInGroupService(StudentsInGroupRepository studentsInGroupRepository,  StudentRepository studentRepository, StudentGroupRepository studentGroupRepository) {
        this.studentsInGroupRepository = studentsInGroupRepository;
        this.studentRepository = studentRepository;
        this.studentGroupRepository = studentGroupRepository;
    }

    public List<StudentInGroup> getStudentsByGroupId(UUID groupId) {
        try {
            List<StudentInGroup> studentsInGroup = studentsInGroupRepository.findAllByStudentGroupGroupId(groupId);

            return studentsInGroup;
        }catch (Exception e){
            throw new InternalServerErrorException("An error occurred while fetching the students: " + e.getMessage());
        }
    }

    public List<StudentInGroup> findAll() {
        try {
            List<StudentInGroup> studentsInGroup = studentsInGroupRepository.findAll();

            return studentsInGroup;
        }catch (Exception e){
            throw new InternalServerErrorException("An error occurred while fetching the students: " + e.getMessage());
        }
    }

    public String addStudentToGroup(UUID groupId, UUID studentId) {
        try {
            Optional<StudentGroup>  studentGroup=studentGroupRepository.findById(groupId);
            if (studentGroup.isEmpty()) {
                throw new ResourceNotFoundException("StudentGroup", "groupId", groupId.toString());
            }
            Optional<Student>  student=studentRepository.findById(studentId);
            if (student.isEmpty()) {
                throw new ResourceNotFoundException("Student", "student id", studentId.toString());
            }
            StudentInGroup studentInGroup = new StudentInGroup();
            studentInGroup.setStudent(student.get());
            studentInGroup.setStudentGroup(studentGroup.get());
            studentsInGroupRepository.save(studentInGroup);
            return "Successfully added";
        }catch (Exception e){
            throw new InternalServerErrorException("An error occurred while adding the student: " + e.getMessage());
        }
    }
    @Transactional
    public String deleteStudentFromGroup( UUID studentId) {
        try {
            Optional<Student>  student=studentRepository.findById(studentId);
            if (student.isEmpty()) {
                throw new ResourceNotFoundException("Student", "studentNo", studentId.toString());
            }
            studentsInGroupRepository.deleteByStudentStudentId(studentId);
            return "Student successfully deleted from group ";
        }catch (Exception e){
            throw new InternalServerErrorException("An error occurred while deleting the student: " + e.getMessage());
        }
    }
    private StudentInGroupDto mapToDto(StudentInGroup studentInGroup){
        StudentInGroupDto  studentInGroupDto=new StudentInGroupDto();
        studentInGroupDto.setStudentGroup(studentInGroup.getStudentGroup());
        studentInGroupDto.setId(studentInGroup.getId());
        studentInGroupDto.setStudent(studentInGroup.getStudent());
        return  studentInGroupDto;
    }
    private StudentInGroup mapToEntity(StudentInGroupDto studentInGroupDto){
        StudentInGroup  studentInGroup=new StudentInGroup();
        studentInGroup.setStudentGroup(studentInGroupDto.getStudentGroup());
        studentInGroup.setId(studentInGroupDto.getId());
        studentInGroup.setStudent(studentInGroupDto.getStudent());
        return  studentInGroup;
    }


    public List<Student> getStudentsOfLecturer(UUID lecturerId) {
        try {
            Optional<StudentGroup> studentsGroup = studentGroupRepository.findByLecturerLecturerId(lecturerId);
            List<StudentInGroup> studentInGroupList = studentsInGroupRepository.findAllByStudentGroup_GroupId(studentsGroup.get().getGroupId());
            List<Student> studentList = studentInGroupList.stream().map(studentInGroup -> (studentInGroup.getStudent())).collect(Collectors.toList());
            return studentList;
        }catch (Exception e){
            throw new InternalServerErrorException("An error occurred while fetching the students: " + e.getMessage());
        }
    }
}
