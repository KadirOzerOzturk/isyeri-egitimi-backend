package com.isyeriegitimi.backend.controller;

import com.isyeriegitimi.backend.dto.LecturerDto;
import com.isyeriegitimi.backend.model.ApiResponse;
import com.isyeriegitimi.backend.model.Lecturer;
import com.isyeriegitimi.backend.service.LecturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/lecturers")
public class LecturerController {

    private final LecturerService lecturerService;

    @Autowired
    public LecturerController(LecturerService lecturerService) {
        this.lecturerService = lecturerService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllLecturer(){
        return ResponseEntity.ok(ApiResponse.success(lecturerService.getAllLecturers(),"Lecturers fetched successfully"));
    }
    @GetMapping("/{lecturerId}")
    public ResponseEntity<ApiResponse<Lecturer>> getLecturerById(@PathVariable UUID lecturerId){

        return ResponseEntity.ok(ApiResponse.success(lecturerService.getLecturerByLecturerId(lecturerId).get(),"Lecturer fetched successfully"));
    }
    @PutMapping("/update/{lecturerId}")
    public ResponseEntity<?> updateLecturer(@PathVariable UUID lecturerId , @RequestBody LecturerDto lecturerDto){

            lecturerService.updateLecturer(lecturerId,lecturerDto);
            return ResponseEntity.ok(ApiResponse.success(null,"Lecturer updated successfully"));

    }
    @GetMapping("/getLecturerOfStudent/{studentId}")
    public ResponseEntity<?> getLecturerOfStudentByStudentNo(@PathVariable UUID studentId){

         return ResponseEntity.ok(ApiResponse.success(lecturerService.getLecturerOfStudentByStudentId(studentId),"Lecturer fetched successfully"));

    }
    @PostMapping("/saveLecturer")
    public ResponseEntity<ApiResponse<?>> saveLecturer(@RequestBody LecturerDto lecturerDto) {
        return ResponseEntity.ok(ApiResponse.success(lecturerService.save(lecturerDto), "Lecturer created successfully"));
    }
}
