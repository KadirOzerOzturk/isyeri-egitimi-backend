package com.isyeriegitimi.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.Converter;
import com.isyeriegitimi.backend.dto.FileInfoDto;
import com.isyeriegitimi.backend.model.ApiResponse;
import com.isyeriegitimi.backend.model.FileInfo;
import com.isyeriegitimi.backend.service.FileService;
import jdk.jfr.StackTrace;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<?>> uploadFile(@RequestBody FileInfo fileInfo){

        return ResponseEntity.ok(ApiResponse.success(fileService.uploadFile(fileInfo), "File uploaded successfully"));
    }
    @GetMapping("/{userId}/{userRole}/{fileName}")
    public ResponseEntity<ApiResponse<FileInfoDto>> getFile(@PathVariable UUID userId, @PathVariable String userRole, @PathVariable String fileName){
        return ResponseEntity.ok(ApiResponse.success(fileService.getFile(userId,userRole,fileName), "File fetched successfully"));
    }


    @GetMapping("/all")
    public ResponseEntity<ApiResponse<?>> getFiles(){
        return ResponseEntity.ok(ApiResponse.success(fileService.getFiles(), "Files fetched successfully"));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getFile(@PathVariable UUID id){
        return ResponseEntity.ok(ApiResponse.success(fileService.getFileById(id), "File fetched successfully"));
    }



}
