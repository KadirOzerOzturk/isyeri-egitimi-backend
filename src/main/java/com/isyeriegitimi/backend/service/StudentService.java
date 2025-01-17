package com.isyeriegitimi.backend.service;

import com.isyeriegitimi.backend.dto.LecturerDto;
import com.isyeriegitimi.backend.dto.StudentDto;
import com.isyeriegitimi.backend.exceptions.InternalServerErrorException;
import com.isyeriegitimi.backend.exceptions.ResourceNotFoundException;
import com.isyeriegitimi.backend.model.Student;
import com.isyeriegitimi.backend.model.StudentInGroup;
import com.isyeriegitimi.backend.repository.StudentRepository;
import com.isyeriegitimi.backend.repository.StudentsInGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private StudentRepository studentRepository;
    private StudentsInGroupRepository studentsInGroupRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository, StudentsInGroupRepository studentsInGroupRepository) {
        this.studentRepository = studentRepository;
        this.studentsInGroupRepository = studentsInGroupRepository;
    }

    public Optional<Student> getStudentByStudentNo(String studentNo){
        return studentRepository.findByStudentNumber(studentNo);
    }
    public String save(StudentDto studentDto ,String studentNo) {

            try {
                Optional<Student> existingStudent = studentRepository.findByStudentNumber(studentNo);
                if (existingStudent.isPresent()) {
                    Student student = new Student();
                    student.setStudentNumber(studentDto.getStudentNumber());
                    student.setFirstName(studentDto.getFirstName());
                    student.setLastName(studentDto.getLastName());
                    student.setEmail(studentDto.getEmail());
                    student.setPhoneNumber(studentDto.getPhoneNumber());
                    student.setGpa(studentDto.getGpa());
                    student.setGrade(studentDto.getGrade());
                    student.setFaculty(studentDto.getFaculty());
                    student.setAbout(studentDto.getAbout());
                    student.setCompany(studentDto.getCompany());
                    studentRepository.save(student);
                }else{
                    throw new ResourceNotFoundException("Student", "studentNo", studentNo);
                }
                return "Student updated successfully";
            }catch (Exception e){
                throw new InternalServerErrorException("An error occurred while updating the student: " + e.getMessage());
            }
    }
    public List<StudentDto> getAllStudents() {
        try {
            List<Student> studentList = studentRepository.findAll();
            if (studentList.isEmpty()) {
                throw new ResourceNotFoundException("Student", "studentList", null);
            }
            List<StudentDto> studentDtoList = studentList.stream()
                    .map(student -> mapToDto(student))
                    .collect(Collectors.toList());
            return studentDtoList;
        }catch (Exception e){
            throw new InternalServerErrorException("An error occurred while fetching the students: " + e.getMessage());
        }
    }

    public List<StudentDto> getAllStudentsWithoutGroup() {
        try {
            List<Student> studentList = studentRepository.findAll();
            if (studentList.isEmpty()) {
                throw new ResourceNotFoundException("StudentList");
            }
            List<StudentInGroup> studentInGroupList = studentsInGroupRepository.findAll();
            if (studentInGroupList.isEmpty()) {
                throw new ResourceNotFoundException("StudentInGroupList");
            }
            Set<String> studentNumbersInGroup = studentInGroupList.stream()
                    .map(studentInGroup -> studentInGroup.getStudent().getStudentNumber())
                    .collect(Collectors.toSet());

            List<Student> studentsWithoutGroup = studentList.stream()
                    .filter(student -> !studentNumbersInGroup.contains(student.getStudentNumber()))
                    .collect(Collectors.toList());
            List<StudentDto> studentDtoList =studentsWithoutGroup.stream().map(student -> mapToDto(student)).collect(Collectors.toList());

            return studentDtoList;
        }catch (Exception e){
            throw new InternalServerErrorException("An error occurred while fetching the students: " + e.getMessage());
        }
    }
    public Student mapDtoToEntity(StudentDto studentDto) {
        if (studentDto == null) {
            return null;
        }

        Student student = new Student();
        student.setStudentNumber(studentDto.getStudentNumber());
        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());
        student.setEmail(studentDto.getEmail());
        student.setPhoneNumber(studentDto.getPhoneNumber());
        student.setGpa(studentDto.getGpa());
        student.setGrade(studentDto.getGrade());
        student.setFaculty(studentDto.getFaculty());
        student.setAbout(studentDto.getAbout());
        student.setCompany(studentDto.getCompany());


        return student;
    }

    public StudentDto mapToDto(Student student) {
        if (student == null) {
            return null;
        }

        StudentDto studentDto = new StudentDto();
        studentDto.setStudentNumber(student.getStudentNumber());
        studentDto.setFirstName(student.getFirstName());
        studentDto.setLastName(student.getLastName());
        studentDto.setEmail(student.getEmail());
        studentDto.setPhoneNumber(student.getPhoneNumber());
        studentDto.setGpa(student.getGpa());
        studentDto.setGrade(student.getGrade());
        studentDto.setFaculty(student.getFaculty());
        studentDto.setAbout(student.getAbout());
        studentDto.setCompany(student.getCompany());


        return studentDto;
    }


}
