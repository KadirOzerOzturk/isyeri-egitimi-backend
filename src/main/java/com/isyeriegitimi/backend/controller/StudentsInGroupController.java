package com.isyeriegitimi.backend.controller;

import com.isyeriegitimi.backend.dto.StudentInGroupDto;
import com.isyeriegitimi.backend.model.ApiResponse;
import com.isyeriegitimi.backend.model.Student;
import com.isyeriegitimi.backend.model.StudentInGroup;
import com.isyeriegitimi.backend.service.StudentsInGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/studentsInGroup")
public class StudentsInGroupController {

    private final StudentsInGroupService studentsInGroupService;

    @Autowired
    public StudentsInGroupController(StudentsInGroupService studentsInGroupService) {
        this.studentsInGroupService = studentsInGroupService;
    }

    @GetMapping("/getStudents/{groupId}")
    public ResponseEntity<ApiResponse<List<StudentInGroup>>>  getStudentsBbyGroupId(@PathVariable UUID groupId){
        return ResponseEntity.ok(ApiResponse.success(studentsInGroupService.getStudentsByGroupId(groupId),"Students fetched successfully"));
    }
    @GetMapping("/getAllStudents/")
    public ResponseEntity<ApiResponse<List<StudentInGroup>>>  getAllStudents(){
        return ResponseEntity.ok(ApiResponse.success(studentsInGroupService.findAll(),"Students fetched successfully"));
    }
    @PostMapping("/addStudent/{studentId}/{groupId}")
    public ResponseEntity<ApiResponse<?>> addStudentToGroup(@PathVariable UUID groupId, @PathVariable UUID studentId){
        return ResponseEntity.ok(ApiResponse.success(studentsInGroupService.addStudentToGroup(groupId,studentId),"Student added successfully"));

    }
    @DeleteMapping("/deleteStudent/{studentId}")
    public ResponseEntity<?> deleteStudentFromGroup(@PathVariable UUID studentId){
            return ResponseEntity.ok(ApiResponse.success(studentsInGroupService.deleteStudentFromGroup(studentId),"Student deleted successfully"));
    }
    @GetMapping("/getStudentsByOfLecturer/{lecturerId}")
    public ResponseEntity<ApiResponse<List<Student>>>  getStudentsByOfLecturer(@PathVariable UUID lecturerId){
        return ResponseEntity.ok(ApiResponse.success(studentsInGroupService.getStudentsOfLecturer(lecturerId),"Students fetched successfully"));
    }

}
