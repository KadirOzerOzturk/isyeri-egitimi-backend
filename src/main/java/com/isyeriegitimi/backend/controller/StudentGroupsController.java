package com.isyeriegitimi.backend.controller;


import com.isyeriegitimi.backend.model.ApiResponse;
import com.isyeriegitimi.backend.model.StudentGroup;
import com.isyeriegitimi.backend.service.StudentGroupsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/groups")
public class StudentGroupsController {

    private final StudentGroupsService   studentGroupsService;

    @Autowired
    public StudentGroupsController(StudentGroupsService studentGroupsService) {
        this.studentGroupsService = studentGroupsService;
    }
    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllGroups(){
        return ResponseEntity.ok( ApiResponse.success(studentGroupsService.getAllGroups(),"Groups fetched successfully"));
    }
    @GetMapping("/{lecturerId}")
    public ResponseEntity<ApiResponse<List<StudentGroup>>> getGroupsByLecturerId(@PathVariable UUID lecturerId){
        return ResponseEntity.ok(ApiResponse.success(studentGroupsService.getGroupsByLecturerId(lecturerId),"Groups fetched successfully"));

    }
    @PostMapping("/createGroup/{lecturerId}")
    public ResponseEntity<ApiResponse<?>> createGroup(@PathVariable UUID lecturerId){
            return  ResponseEntity.ok(ApiResponse.success(studentGroupsService.createGroup(lecturerId),"Group created successfully"));
    }
    @DeleteMapping("/deleteGroup/{groupId}")
    public ResponseEntity<ApiResponse<?>> deleteGroup(@PathVariable UUID groupId){
            return  ResponseEntity.ok(ApiResponse.success(studentGroupsService.deleteGroup(groupId),"Group deleted successfully"));
    }
    @GetMapping("/autoCreate")
    public ResponseEntity<ApiResponse<?>> autoCreateGroups(){
        return ResponseEntity.ok(ApiResponse.success(studentGroupsService.autoCreate(),"Groups created successfully"));
    }

}
