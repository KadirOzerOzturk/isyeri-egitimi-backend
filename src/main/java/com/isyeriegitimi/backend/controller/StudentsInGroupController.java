package com.isyeriegitimi.backend.controller;

import com.isyeriegitimi.backend.dto.StudentInGroupDto;
import com.isyeriegitimi.backend.model.StudentInGroup;
import com.isyeriegitimi.backend.service.StudentsInGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/studentsInGroup")
public class StudentsInGroupController {

    private final StudentsInGroupService studentsInGroupService;

    @Autowired
    public StudentsInGroupController(StudentsInGroupService studentsInGroupService) {
        this.studentsInGroupService = studentsInGroupService;
    }

    @GetMapping("/getStudents/{groupId}")
    public ResponseEntity<List<StudentInGroup>>  getStudentsBbyGroupId(@PathVariable int groupId){
        return ResponseEntity.ok(studentsInGroupService.getStudentsByGroupId(groupId));
    }
    @GetMapping("/getAllStudents/")
    public ResponseEntity<List<StudentInGroup>>  getAllStudents(){
        return ResponseEntity.ok(studentsInGroupService.findAll());
    }
    @PostMapping("/addStudent/{studentNo}/{groupId}")
    public ResponseEntity<?> addStudentToGroup(@PathVariable Long groupId,@PathVariable Long studentNo){
        try {
            return ResponseEntity.ok(studentsInGroupService.addStudentToGroup(groupId,studentNo));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @DeleteMapping("/deleteStudent/{studentNo}")
    public ResponseEntity<?> deleteStudentFromGroup(@PathVariable Long studentNo){
        try {
            return ResponseEntity.ok(studentsInGroupService.deleteStudent(studentNo));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
