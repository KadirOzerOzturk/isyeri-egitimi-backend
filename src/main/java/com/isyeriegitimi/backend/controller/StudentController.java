package com.isyeriegitimi.backend.controller;

import com.isyeriegitimi.backend.dto.StudentDto;
import com.isyeriegitimi.backend.model.ApiResponse;
import com.isyeriegitimi.backend.model.Student;
import com.isyeriegitimi.backend.service.StudentService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/students")
public class StudentController {

    public StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/getByStudentNo/{studentNo}")
    public ResponseEntity<ApiResponse<Optional<Student>>> getStudentByStudentNo(@PathVariable String studentNo){

        return ResponseEntity.ok(ApiResponse.success(studentService.getStudentByStudentNo(studentNo),"Student fetched successfully"));
    }
    @GetMapping("/{studentId}")
    public ResponseEntity<ApiResponse<Optional<Student>>> getStudentByStudentId(@PathVariable UUID studentId){

        return ResponseEntity.ok(ApiResponse.success(studentService.getStudentByStudentId(studentId),"Student fetched successfully"));
    }
    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse<List<StudentDto>>> getAllStudents(){

        return ResponseEntity.ok(ApiResponse.success(studentService.getAllStudents(),"Students fetched successfully"));
    }
    @GetMapping("/getAllStudentsWithoutGroup")
    public ResponseEntity<ApiResponse<List<StudentDto>>> getAllStudentsWithoutGroup(){

        return ResponseEntity.ok(ApiResponse.success(studentService.getAllStudentsWithoutGroup(),"Students fetched successfully"));
    }


    @PutMapping("/update/{studentId}")
    public ResponseEntity<ApiResponse<String>> updateUser(@RequestBody StudentDto studentDto, @PathVariable UUID  studentId) {

            studentService.update(studentDto,studentId);
            return ResponseEntity.ok(ApiResponse.success(null,"Student updated successfully"));

    }
     @PostMapping("/saveStudent")
     public ResponseEntity<ApiResponse<?>> saveStudent(@RequestBody StudentDto studentDto) {
                return ResponseEntity.ok(ApiResponse.success( studentService.save(studentDto), "Student created successfully"));
     }


}
