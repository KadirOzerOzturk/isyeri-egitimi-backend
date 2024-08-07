package com.isyeriegitimi.backend.service;

import com.isyeriegitimi.backend.dto.StudentGroupDto;
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




    public List<StudentGroup> getGroupsByLecturerId(Long lecturerId) {
        return studentGroupRepository.findAllByIzleyiciIzleyiciId(lecturerId);
    }


    public List<StudentGroup> getAllGroups() {

        return studentGroupRepository.findAll();

    }

    public String createGroup(Long lectuererId) {
        Optional<Lecturer> lecturer=lecturerRepository.findById(lectuererId);
        if (lecturer.isPresent()){
            StudentGroup studentGroup=new StudentGroup();
            studentGroup.setIzleyici(lecturer.get());
            studentGroupRepository.save(studentGroup);
            }
        return "Successfully saved";

    }
    public String  deleteGroup(Long groupId) {
        studentsInGroupRepository.deleteAllByStudentGroupGrupId(groupId);
        studentGroupRepository.deleteById(groupId);
        return "Group successfully deleted.";
    }
    private StudentGroup mapToEntity(StudentGroupDto studentGroupDto){
        StudentGroup studentGroup=new StudentGroup();
        studentGroup.setGrupId(studentGroupDto.getGrupId());
        studentGroup.setIzleyici(studentGroupDto.getIzleyici());
        return studentGroup;
    }
    private StudentGroupDto mapToDto(StudentGroup studentGroup){
        StudentGroupDto studentGroupDto=new StudentGroupDto();
        studentGroupDto.setGrupId(studentGroup.getGrupId());
        studentGroupDto.setIzleyici(studentGroup.getIzleyici());
        return studentGroupDto;
    }


}
