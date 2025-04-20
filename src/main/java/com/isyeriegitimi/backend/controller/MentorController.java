package com.isyeriegitimi.backend.controller;

import com.isyeriegitimi.backend.dto.MentorDto;
import com.isyeriegitimi.backend.dto.StudentDto;
import com.isyeriegitimi.backend.model.ApiResponse;
import com.isyeriegitimi.backend.repository.MentorRepository;
import com.isyeriegitimi.backend.security.service.UserService;
import com.isyeriegitimi.backend.service.MentorService;
import com.isyeriegitimi.backend.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/mentors")
public class MentorController {

    @Autowired
    private MentorService mentorService;
    @Autowired
    private UserService userService;
    @Autowired
    private StudentService studentService;


    @PostMapping("/save")
    public ResponseEntity<ApiResponse<UUID>> saveMentor(@RequestBody MentorDto mentorDto) {
        return ResponseEntity.ok(ApiResponse.success(mentorService.createMentor(mentorDto),"Mentor created successfully"));
    }
    @PutMapping("/update/{mentorId}")
    public ResponseEntity<ApiResponse<?>> updateMentor(@RequestBody MentorDto mentorDto,@PathVariable UUID mentorId) {
        mentorService.updateMentor(mentorDto,mentorId);
        return ResponseEntity.ok(ApiResponse.success(null,"Mentor updated successfully"));
    }
    @GetMapping("/{mentorId}/students")
    public ResponseEntity<ApiResponse<List<StudentDto>>> getStudents(@PathVariable UUID mentorId) {
        return ResponseEntity.ok(ApiResponse.success(mentorService.getStudentsByMentor(mentorId),"Students fetched successfully"));
    }
    @GetMapping("/company/{companyId}")
    public ResponseEntity<ApiResponse<List<MentorDto>>> getStudentsByCompany(@PathVariable UUID companyId) {
        return ResponseEntity.ok(ApiResponse.success(mentorService.getMentorsByCompanyId(companyId),"Mentors fetched successfully"));
    }
    @PostMapping("/assignMentor/{studentId}/{mentorId}")
    public ResponseEntity<ApiResponse<?>>  assignMentorToStudent(
            @PathVariable UUID studentId,
            @PathVariable UUID mentorId
    ) {
        studentService.assignMentorToStudent(studentId,mentorId);
        return ResponseEntity.ok(ApiResponse.success(null,"Mentor assigned successfully."));
    }
}
