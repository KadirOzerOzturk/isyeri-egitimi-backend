package com.isyeriegitimi.backend.controller;

import com.isyeriegitimi.backend.dto.StudentDto;
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
    public Optional<Student> getStudentByStudentNo(@PathVariable Long studentNo){

        return studentService.getStudentByStudentNo(studentNo);
    }
    @GetMapping("/getAll")
    public ResponseEntity<List<StudentDto>> getAllStudents(){

        return ResponseEntity.ok(studentService.getAllStudents());
    }
    @GetMapping("/getAllStudentsWithoutGroup")
    public ResponseEntity<List<StudentDto>> getAllStudentsWithoutGroup(){

        return ResponseEntity.ok(studentService.getAllStudentsWithoutGroup());
    }


    @PutMapping("/update/{studentNo}")
    public ResponseEntity<String> updateUser(@RequestBody StudentDto studentDto, @PathVariable Long studentNo) {
        try {
            studentService.save(studentDto,studentNo);
            return ResponseEntity.ok("Successfully updated");
        } catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

//    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<String> saveStudent(@ModelAttribute StudentDto studentDto,
//                                              @RequestParam("file") MultipartFile file) {
//        studentService.saveStudentWithPhoto(studentDto, file);
//        return ResponseEntity.ok("Student saved successfully.");
//    }
}
