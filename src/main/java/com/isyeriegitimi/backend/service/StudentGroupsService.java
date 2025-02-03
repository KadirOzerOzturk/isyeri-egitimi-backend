package com.isyeriegitimi.backend.service;

import com.isyeriegitimi.backend.dto.StudentGroupDto;
import com.isyeriegitimi.backend.exceptions.InternalServerErrorException;
import com.isyeriegitimi.backend.exceptions.ResourceNotFoundException;
import com.isyeriegitimi.backend.model.Lecturer;
import com.isyeriegitimi.backend.model.Student;
import com.isyeriegitimi.backend.model.StudentGroup;
import com.isyeriegitimi.backend.repository.LecturerRepository;
import com.isyeriegitimi.backend.repository.StudentGroupRepository;
import com.isyeriegitimi.backend.repository.StudentsInGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StudentGroupsService {

    private  final StudentGroupRepository studentGroupRepository;
    private  final LecturerRepository lecturerRepository;
    private final StudentsInGroupRepository studentsInGroupRepository;

    @Autowired
    public StudentGroupsService(StudentGroupRepository studentGroupRepository, LecturerRepository lecturerRepository, StudentsInGroupRepository studentsInGroupRepository) {
        this.studentGroupRepository = studentGroupRepository;
        this.lecturerRepository = lecturerRepository;
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


}
