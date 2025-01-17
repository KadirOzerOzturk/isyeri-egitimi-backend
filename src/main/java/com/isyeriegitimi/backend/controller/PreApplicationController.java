package com.isyeriegitimi.backend.controller;

import com.isyeriegitimi.backend.model.ApiResponse;
import com.isyeriegitimi.backend.model.PreApplication;
import com.isyeriegitimi.backend.service.PreApplicationService;
//import com.itextpdf.kernel.geom.PageSize;
//import com.itextpdf.layout.Document;
//import com.itextpdf.layout.element.Paragraph;
//import com.itextpdf.layout.element.Table;
//import com.itextpdf.layout.element.Cell;
//import com.itextpdf.layout.property.UnitValue;
//import com.itextpdf.kernel.pdf.PdfWriter;
//import com.itextpdf.kernel.pdf.PdfDocument;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/pre-application")
public class PreApplicationController {
    @Autowired
    private PreApplicationService service;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createPreApplication(@RequestBody PreApplication entry) {
        service.savePreApplication(entry);
        return ResponseEntity.ok(ApiResponse.success(null, "Pre-application form saved successfully"));
    }

    @GetMapping
    public ResponseEntity<List<PreApplication>> getAllFormEntries() {
        return ResponseEntity.ok(service.getAllFormEntries());
    }

    @GetMapping("/download-excel")
    public ResponseEntity<Resource> downloadExcel() {
        List<PreApplication> applications = service.getAllFormEntries();

        service.writeToExcel(applications);
        Resource resource = new FileSystemResource("PreApplications.xlsx");
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"PreApplications.xlsx\"")
                .body(resource);
    }




}
