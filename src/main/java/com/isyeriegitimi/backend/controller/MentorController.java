package com.isyeriegitimi.backend.controller;

import com.isyeriegitimi.backend.dto.MentorDto;
import com.isyeriegitimi.backend.model.ApiResponse;
import com.isyeriegitimi.backend.repository.MentorRepository;
import com.isyeriegitimi.backend.service.MentorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/mentors")
public class MentorController {

    @Autowired
    private MentorService mentorService;


    @PostMapping("/save")
    public ResponseEntity<ApiResponse<UUID>> saveMentor(@RequestBody MentorDto mentorDto) {
        return ResponseEntity.ok(ApiResponse.success(mentorService.createMentor(mentorDto),"Mentor created successfully"));
    }
}
