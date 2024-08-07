package com.isyeriegitimi.backend.controller;

import com.isyeriegitimi.backend.dto.LecturerDto;
import com.isyeriegitimi.backend.model.Lecturer;
import com.isyeriegitimi.backend.model.Student;
import com.isyeriegitimi.backend.service.LecturerService;
import com.isyeriegitimi.backend.service.StudentsInGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/lecturer")
public class LecturerController {

    private final LecturerService lecturerService;

    @Autowired
    public LecturerController(LecturerService lecturerService) {
        this.lecturerService = lecturerService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllLecturer(){

        try {
            return ResponseEntity.ok( lecturerService.getAllLecturer());
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @GetMapping("/{lecturerId}")
    public Optional<Lecturer> getLecturerById(@PathVariable Long lecturerId){

        return lecturerService.getLecturerByLecturerId(lecturerId);
    }
    @PutMapping("/update/{lecturerId}")
    public ResponseEntity<?> updateLecturer(@PathVariable Long lecturerId , @RequestBody LecturerDto lecturerDto){

        try {
            lecturerService.update(lecturerId,lecturerDto);
            return ResponseEntity.ok("Successfully updated");
        }catch (Exception e){

                return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @GetMapping("/getLecturerOfStudent/{studentNo}")
    public ResponseEntity<?> getLecturerOfStudentByStudentNo(@PathVariable Long studentNo){
        try {
            return ResponseEntity.ok(lecturerService.getLecturerOfStudentByStudentNo(studentNo));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Student has not a group");
        }
    }
}
