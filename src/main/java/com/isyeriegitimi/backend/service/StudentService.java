package com.isyeriegitimi.backend.service;

import com.isyeriegitimi.backend.dto.LecturerDto;
import com.isyeriegitimi.backend.dto.StudentDto;
import com.isyeriegitimi.backend.exceptions.InternalServerErrorException;
import com.isyeriegitimi.backend.exceptions.ResourceNotFoundException;
import com.isyeriegitimi.backend.model.Company;
import com.isyeriegitimi.backend.model.Mentor;
import com.isyeriegitimi.backend.model.Student;
import com.isyeriegitimi.backend.model.StudentInGroup;
import com.isyeriegitimi.backend.repository.MentorRepository;
import com.isyeriegitimi.backend.repository.StudentRepository;
import com.isyeriegitimi.backend.repository.StudentsInGroupRepository;
import com.isyeriegitimi.backend.security.dto.UserDto;
import com.isyeriegitimi.backend.security.dto.UserRequest;
import com.isyeriegitimi.backend.security.enums.Role;
import com.isyeriegitimi.backend.security.model.User;
import com.isyeriegitimi.backend.security.service.AuthenticationService;
import com.isyeriegitimi.backend.security.service.UserService;
import jakarta.transaction.Transactional;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private StudentRepository studentRepository;

    private StudentsInGroupRepository studentsInGroupRepository;
    private UserService userService;
    private MentorRepository mentorRepository;

    public StudentService(StudentRepository studentRepository, StudentsInGroupRepository studentsInGroupRepository, UserService userService, MentorRepository mentorRepository) {
        this.studentRepository = studentRepository;
        this.studentsInGroupRepository = studentsInGroupRepository;
        this.userService = userService;
        this.mentorRepository = mentorRepository;
    }

    public Optional<Student> getStudentByStudentNo(String studentNo){
        try {
            Optional<Student> student = studentRepository.findByStudentNumber(studentNo);
            if (student.isEmpty()) {
                throw new ResourceNotFoundException("Student", "Student Number", studentNo);
            }
            return student;
        }catch (Exception e){
            throw new InternalServerErrorException("An error occurred while fetching the student: " + e.getMessage());

        }
    }
    public Optional<Student> getStudentByStudentId(UUID studentId) {
        try {
            Optional<Student> student = studentRepository.findById(studentId);
            if (student.isEmpty()) {
                throw new ResourceNotFoundException("Student", "Student ID", studentId.toString());
            }
            return student;
        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred while fetching the student: " + e.getMessage());
        }
    }
    @Transactional
    public String update(StudentDto studentDto, UUID studentId) {
        try {
            Optional<Student> existingStudentOpt = studentRepository.findById(studentId);
            if (existingStudentOpt.isPresent()) {
                Student existingStudent = existingStudentOpt.get();

                // Check if the email has changed
                String oldEmail = existingStudent.getEmail();
                String newEmail = studentDto.getEmail();

                existingStudent.setStudentNumber(studentDto.getStudentNumber());
                existingStudent.setFirstName(studentDto.getFirstName());
                existingStudent.setLastName(studentDto.getLastName());
                existingStudent.setEmail(newEmail); // Update email
                existingStudent.setPhoneNumber(studentDto.getPhoneNumber());
                existingStudent.setGpa(studentDto.getGpa());
                existingStudent.setPassword(existingStudent.getPassword());
                existingStudent.setGrade(studentDto.getGrade());
                existingStudent.setFaculty(studentDto.getFaculty());
                existingStudent.setAbout(studentDto.getAbout());
                existingStudent.setCompany(studentDto.getCompany());

                studentRepository.save(existingStudent);

                if (!oldEmail.equals(newEmail)) {
                    System.out.println("Updating user email from " + oldEmail + " to " + newEmail);
                    userService.updateUsernameByEmail(oldEmail, newEmail);
                }

                return "Student updated successfully";
            } else {
                throw new ResourceNotFoundException("Student", "Student ID", studentId.toString());
            }
        } catch (Exception e) {
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

            List<StudentInGroup> studentInGroupList = studentsInGroupRepository.findAll();

            Set<UUID> studentNumbersInGroup = studentInGroupList.stream()
                    .map(studentInGroup -> studentInGroup.getStudent().getStudentId())
                    .collect(Collectors.toSet());

            List<Student> studentsWithoutGroup = studentList.stream()
                    .filter(student -> !studentNumbersInGroup.contains(student.getStudentId()))
                    .collect(Collectors.toList());
            List<StudentDto> studentDtoList =studentsWithoutGroup.stream().map(student -> mapToDto(student)).collect(Collectors.toList());

            return studentDtoList;
        }catch (Exception e){
            throw new InternalServerErrorException("An error occurred while fetching the students: " + e.getMessage());
        }
    }
    public UUID save(StudentDto studentDto) {
        try {
            Student student = mapDtoToEntity(studentDto);
            studentRepository.save(student);

            return student.getStudentId();
        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred while saving the student: " + e.getMessage());
        }
    }
    public static Student mapDtoToEntity(StudentDto studentDto) {
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
        student.setPassword(studentDto.getPassword());
        student.setMentor(studentDto.getMentor());
        return student;
    }

    public static StudentDto mapToDto(Student student) {
        if (student == null) {
            return null;
        }
        StudentDto studentDto = new StudentDto();
        studentDto.setStudentId(student.getStudentId());
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
        studentDto.setIdentityNumber(student.getIdentityNumber());
        studentDto.setMentor(student.getMentor());


        return studentDto;
    }


    public List<StudentDto> getStudentsByCompanyId(UUID companyId) {
        try {
            List<Student> studentList = studentRepository.findByCompanyCompanyId(companyId);
            if (studentList.isEmpty()){
                return Collections.emptyList();
            }
            List<StudentDto> studentDtoList = studentList.stream()
                    .map(student -> mapToDto(student))
                    .collect(Collectors.toList());
            return studentDtoList;
        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred while fetching the students: " + e.getMessage());
        }
    }

    public void assignMentorToStudent(UUID studentId, UUID mentorId) {
        try {
            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Student","id",studentId.toString()));

            Mentor mentor = mentorRepository.findById(mentorId)
                    .orElseThrow(() -> new ResourceNotFoundException("Mentor","id",mentorId.toString()));

            student.setMentor(mentor);
            studentRepository.save(student);
        }catch (Exception e){
            throw new InternalServerErrorException("An error occurred while fetching the students: " + e.getMessage());
        }
    }

    public List<StudentDto> getAllStudentsWithCompany() {
        try{

            List<Student> studentList = studentRepository.findAllByCompanyIsNotNull();
            if (studentList.isEmpty()){
                return Collections.emptyList();
            }
            List<StudentDto> studentDtoList = studentList.stream()
                    .map(student -> mapToDto(student))
                    .collect(Collectors.toList());
            return studentDtoList;
        }catch (Exception e){
            throw new InternalServerErrorException("An error occurred while fetching the students: " + e.getMessage());
        }
    }
}
