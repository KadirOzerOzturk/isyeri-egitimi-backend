package com.isyeriegitimi.backend.controller;

import com.isyeriegitimi.backend.dto.StudentDto;
import com.isyeriegitimi.backend.model.ApiResponse;
import com.isyeriegitimi.backend.model.Student;
import com.isyeriegitimi.backend.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/students")
public class StudentController {

    public StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/{studentNo}")
    public ResponseEntity<ApiResponse<Optional<Student>>> getStudentByStudentNo(@PathVariable String studentNo){

        return ResponseEntity.ok(ApiResponse.success(studentService.getStudentByStudentNo(studentNo),"Student fetched successfully"));
    }
    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse<List<StudentDto>>> getAllStudents(){

        return ResponseEntity.ok(ApiResponse.success(studentService.getAllStudents(),"Students fetched successfully"));
    }
    @GetMapping("/getAllStudentsWithoutGroup")
    public ResponseEntity<ApiResponse<List<StudentDto>>> getAllStudentsWithoutGroup(){

        return ResponseEntity.ok(ApiResponse.success(studentService.getAllStudentsWithoutGroup(),"Students fetched successfully"));
    }


    @PutMapping("/update/{studentNo}")
    public ResponseEntity<ApiResponse<String>> updateUser(@RequestBody StudentDto studentDto, @PathVariable String studentNo) {

            studentService.save(studentDto,studentNo);
            return ResponseEntity.ok(ApiResponse.success(null,"Student updated successfully"));

    }


}
