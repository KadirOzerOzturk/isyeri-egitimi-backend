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

import java.awt.*;
import java.io.File;
import java.util.List;
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
    @GetMapping("/{userId}/{userRole}")
    public ResponseEntity<ApiResponse<List<FileInfo>>> getFilesByUserId(@PathVariable UUID userId, @PathVariable String userRole){
        return ResponseEntity.ok(ApiResponse.success(fileService.getFilesByUserId(userId,userRole), "File fetched successfully"));
    }
    @GetMapping("/companyPhotos")
    public ResponseEntity<ApiResponse<List<FileInfo>>> getCompanyPhotos(){
        return ResponseEntity.ok(ApiResponse.success(fileService.getCompanyPhotos(), "File fetched successfully"));
    }
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<?>> getFiles(){
        return ResponseEntity.ok(ApiResponse.success(fileService.getFiles(), "Files fetched successfully"));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getFile(@PathVariable UUID id){
        return ResponseEntity.ok(ApiResponse.success(fileService.getFileById(id), "File fetched successfully"));
    }
    @PutMapping("/update/{fileId}")
    public ResponseEntity<ApiResponse<?>> updateFile(@PathVariable UUID fileId,@RequestBody FileInfo fileInfo){
        fileService.updateFile(fileId,fileInfo);
        return ResponseEntity.ok(ApiResponse.success(null,"File updated successfully"));
    }
    @GetMapping("/fileName/{fileName}")
    public ResponseEntity<ApiResponse<?>> getFilesByFileName(@PathVariable String fileName){
        return ResponseEntity.ok(ApiResponse.success(fileService.getFilesByName(fileName),"Files fetched successfully"));
    }
    @DeleteMapping("/delete/{fileId}")
    public ResponseEntity<ApiResponse<?>> deleteFile(@PathVariable UUID fileId){
        fileService.deleteFile(fileId);
        return ResponseEntity.ok(ApiResponse.success(null,"File deleted successfully"));
    }
    @GetMapping("/weeklyReports/{studentId}")
    public ResponseEntity<ApiResponse<List<FileInfo>>> getWeeklyReports(@PathVariable UUID studentId){
        return ResponseEntity.ok(ApiResponse.success(fileService.getWeeklyReportByStudentId(studentId),"Files fetched successfully"));
    }
    @GetMapping("/validate/{barcode}")
    public ResponseEntity<ApiResponse<?>> validateBarcode(@PathVariable String barcode){
        return ResponseEntity.ok(ApiResponse.success(fileService.validateBarcode(barcode),"File founded by barcode"));
    }
}
