package com.isyeriegitimi.backend.service;

import com.isyeriegitimi.backend.dto.StudentGroupDto;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentGroupsService {

    private  final StudentGroupRepository studentGroupRepository;
    private  final LecturerRepository lecturerRepository;
    private final StudentRepository studentRepository;
    private final StudentsInGroupRepository studentsInGroupRepository;

    @Autowired
    public StudentGroupsService(StudentGroupRepository studentGroupRepository, LecturerRepository lecturerRepository, StudentRepository studentRepository, StudentsInGroupRepository studentsInGroupRepository) {
        this.studentGroupRepository = studentGroupRepository;
        this.lecturerRepository = lecturerRepository;
        this.studentRepository = studentRepository;
        this.studentsInGroupRepository = studentsInGroupRepository;
    }



    public List<StudentGroup> getGroupsByLecturerId(UUID lecturerId) {
        try {
            List<StudentGroup> studentGroups = studentGroupRepository.findAllByLecturerLecturerId(lecturerId);

            return studentGroups;
        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred while fetching the student groups: " + e.getMessage());
        }
    }
    public List<StudentGroup> getAllGroups() {
        try {
            return studentGroupRepository.findAll();
        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred while fetching the student groups: " + e.getMessage());

        }
    }
    public String createGroup(UUID lecturerId) {
        try {
            Optional<Lecturer> lecturer = lecturerRepository.findById(lecturerId);
            if (lecturer.isPresent()) {
                StudentGroup studentGroup = new StudentGroup();
                studentGroup.setLecturer(lecturer.get());
                studentGroupRepository.save(studentGroup);
                return "Successfully saved";
            } else {
                throw new ResourceNotFoundException("Lecturer", "lecturerId", lecturerId.toString());
            }
        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred while creating the group: " + e.getMessage());

        }
    }
    public String deleteGroup(UUID groupId) {
        try {
            studentsInGroupRepository.deleteAllByStudentGroupGroupId(groupId);
            studentGroupRepository.deleteById(groupId);
            return "Group successfully deleted.";
        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred while deleting the group: " + e.getMessage());

        }
    }
    private StudentGroup mapToEntity(StudentGroupDto studentGroupDto){
        StudentGroup studentGroup=new StudentGroup();
        studentGroup.setGroupId(studentGroupDto.getGroupId());
        studentGroup.setLecturer(studentGroupDto.getLecturer());
        return studentGroup;
    }
    private StudentGroupDto mapToDto(StudentGroup studentGroup){
        StudentGroupDto studentGroupDto=new StudentGroupDto();
        studentGroupDto.setGroupId(studentGroup.getGroupId());
        studentGroupDto.setLecturer(studentGroup.getLecturer());
        return studentGroupDto;
    }



    public List<StudentGroup> autoCreate() {
        try {
            List<Student> studentList = studentRepository.findAll();

            Map<String, List<Student>> studentsByCompanyAddress = studentList.stream()
                    .filter(student -> student.getCompany() != null && student.getCompany().getAddress() != null)
                    .collect(Collectors.groupingBy(student -> student.getCompany().getAddress()));

            List<StudentGroup> studentGroups = new ArrayList<>();
            Set<Student> alreadyGroupedStudents = studentsInGroupRepository.findAll().stream()
                    .map(StudentInGroup::getStudent)
                    .collect(Collectors.toSet());

            for (Map.Entry<String, List<Student>> entry : studentsByCompanyAddress.entrySet()) {
                List<Student> studentsToGroup = entry.getValue().stream()
                        .filter(student -> !alreadyGroupedStudents.contains(student))
                        .collect(Collectors.toList());

                if (!studentsToGroup.isEmpty()) {
                    StudentGroup studentGroup = StudentGroup.builder().build();
                    studentGroup = studentGroupRepository.save(studentGroup);
                    studentGroups.add(studentGroup);

                    for (Student student : studentsToGroup) {
                        StudentInGroup studentInGroup = StudentInGroup.builder()
                                .studentGroup(studentGroup)
                                .student(student)
                                .build();
                        studentsInGroupRepository.save(studentInGroup);
                    }
                }
            }

            List<Student> ungroupedStudents = studentList.stream()
                    .filter(student -> (student.getCompany() == null || student.getCompany().getAddress() == null)
                            && !alreadyGroupedStudents.contains(student))
                    .collect(Collectors.toList());

            if (!ungroupedStudents.isEmpty()) {
                StudentGroup studentGroup = studentGroups.isEmpty() ? StudentGroup.builder().build() : studentGroups.get(0);

                if (studentGroups.isEmpty()) {
                    studentGroup = studentGroupRepository.save(studentGroup);
                    studentGroups.add(studentGroup);
                }

                for (Student student : ungroupedStudents) {
                    StudentInGroup studentInGroup = StudentInGroup.builder()
                            .studentGroup(studentGroup)
                            .student(student)
                            .build();
                    studentsInGroupRepository.save(studentInGroup);
                }
            }

            return studentGroups;
        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred while creating group: " + e.getMessage());
        }
    }

    public void assignLecturer(UUID groupId, UUID lecturerId) {
        try {
            Optional<StudentGroup> studentGroup = studentGroupRepository.findById(groupId);
            Optional<Lecturer> lecturer =lecturerRepository.findById(lecturerId);
            if (studentGroup.get().getLecturer() != null) {
                throw new InternalServerErrorException("A lecturer is already assigned to the group");
            }else if (studentGroup.isEmpty()){
                throw new ResourceNotFoundException("StudentGroup", "groupId", groupId.toString());
            }else if (lecturer.isEmpty()){
                throw new ResourceNotFoundException("Lecturer", "lecturerId", lecturerId.toString());
            }
            studentGroup.get().setLecturer(lecturer.get());
            studentGroupRepository.save(studentGroup.get());

        }catch (Exception e){
            throw new InternalServerErrorException("An error occurred while assigning the lecturer: " + e.getMessage());
        }
    }
}


