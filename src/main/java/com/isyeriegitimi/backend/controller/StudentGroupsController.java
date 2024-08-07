package com.isyeriegitimi.backend.controller;

import com.isyeriegitimi.backend.dto.StudentGroupDto;
import com.isyeriegitimi.backend.model.Lecturer;
import com.isyeriegitimi.backend.model.Student;
import com.isyeriegitimi.backend.model.StudentGroup;
import com.isyeriegitimi.backend.service.StudentGroupsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groups")
public class StudentGroupsController {

    private final StudentGroupsService   studentGroupsService;

    @Autowired
    public StudentGroupsController(StudentGroupsService studentGroupsService) {
        this.studentGroupsService = studentGroupsService;
    }
    @GetMapping
    public ResponseEntity<?> getAllGroups(){

try {
    return ResponseEntity.ok( studentGroupsService.getAllGroups());
}catch (Exception e){
    return ResponseEntity.internalServerError().body(e.getMessage());
}


    }
    @GetMapping("/{lecturerId}")
    public ResponseEntity<List<StudentGroup>> getGroupsByLecturerId(@PathVariable Long lecturerId){


        return ResponseEntity.ok( studentGroupsService.getGroupsByLecturerId(lecturerId));

    }
    @PostMapping("/createGroup/{lecturerId}")
    public ResponseEntity<?> createGroup(@PathVariable Long lecturerId){
        try {
            return  ResponseEntity.ok(studentGroupsService.createGroup(lecturerId));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @DeleteMapping("/deleteGroup/{groupId}")
    public ResponseEntity<?> deleteGroup(@PathVariable Long groupId){
        try {
            return  ResponseEntity.ok(studentGroupsService.deleteGroup(groupId));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }


}
