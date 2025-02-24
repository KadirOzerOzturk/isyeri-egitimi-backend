package com.isyeriegitimi.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.Converter;
import com.isyeriegitimi.backend.model.ApiResponse;
import com.isyeriegitimi.backend.model.FileInfo;
import com.isyeriegitimi.backend.service.FileService;
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

    @PostMapping(path = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse<?>> uploadFile(
            @RequestPart("fileInfo") FileInfo fileInfo,
            @RequestPart("file") MultipartFile file) {

        return ResponseEntity.ok(ApiResponse.success(fileService.uploadFile(fileInfo, file), "File uploaded successfully"));
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
