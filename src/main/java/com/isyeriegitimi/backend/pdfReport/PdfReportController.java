package com.isyeriegitimi.backend.pdfReport;

import com.isyeriegitimi.backend.dto.FileInfoDto;
import com.isyeriegitimi.backend.model.ApiResponse;
import com.isyeriegitimi.backend.model.DownloadFormRequest;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@RestController
@RequestMapping("/pdf")
public class PdfReportController {

    @Autowired
    private PdfReportService pdfReportService;

    @GetMapping("/download/weekly-report/{studentId}")
    public ResponseEntity<ApiResponse<FileInfoDto>> downloadWeeklyReports(@PathVariable UUID studentId) throws IOException {
        byte[] pdfContents = pdfReportService.generateWeeklyReportPdf(studentId).toByteArray();
        return createFileResponse(pdfContents, "weekly_reports.pdf", "application/pdf");
    }

    @GetMapping("/download/form1/{studentId}/{formId}")
    public ResponseEntity<ApiResponse<FileInfoDto>> downloadForm1(@PathVariable UUID studentId, @PathVariable UUID formId) throws Exception {
        byte[] pdfContents = pdfReportService.generateForm1ByStudentId(formId, studentId).toByteArray();
        return createFileResponse(pdfContents, "form1.pdf", "application/pdf");
    }

    @GetMapping("/download/form2/{studentId}/{formId}")
    public ResponseEntity<ApiResponse<FileInfoDto>> downloadForm2(@PathVariable UUID studentId, @PathVariable UUID formId) throws Exception {
        byte[] pdfContents = pdfReportService.generateForm2ByStudentId(studentId, formId).toByteArray();
        return createFileResponse(pdfContents, "taahutname.pdf", "application/pdf");
    }

    @GetMapping("/download/form3/{studentId}/{formId}")
    public ResponseEntity<ApiResponse<FileInfoDto>> downloadForm3(@PathVariable UUID studentId, @PathVariable UUID formId) throws Exception {
        byte[] pdfContents = pdfReportService.generateForm3ByStudentId(formId, studentId).toByteArray();
        return createFileResponse(pdfContents, "form3.pdf", "application/pdf");
    }

    @GetMapping("/download/form4/{studentId}")
    public ResponseEntity<ApiResponse<FileInfoDto>> downloadForm4(@PathVariable UUID studentId) throws IOException {
        byte[] pdfContents = pdfReportService.generateForm4ByStudentId(studentId).toByteArray();
        return createFileResponse(pdfContents, "form4.pdf", "application/pdf");
    }

    @GetMapping("/download/form4.1/{studentId}")
    public ResponseEntity<ApiResponse<FileInfoDto>> downloadForm4_1(@PathVariable UUID studentId) throws IOException {
        byte[] pdfContents = pdfReportService.generateForm4_1yStudentId(studentId).toByteArray();
        return createFileResponse(pdfContents, "form4.1.pdf", "application/pdf");
    }

    @GetMapping("/download/survey/{surveyId}/student/{studentId}")
    public ResponseEntity<ApiResponse<FileInfoDto>> downloadStudentSurvey(@PathVariable UUID surveyId, @PathVariable UUID studentId) throws Exception {
        byte[] pdfContents = pdfReportService.generateSurveyByStudentId(studentId, surveyId).toByteArray();
        return createFileResponse(pdfContents, "degerlendirmeAnketi.pdf", "application/pdf");
    }

    private ResponseEntity<ApiResponse<FileInfoDto>> createFileResponse(byte[] pdfContents, String fileName, String fileType) {
        String base64Data = Base64.getEncoder().encodeToString(pdfContents);

        FileInfoDto fileInfo = FileInfoDto.builder()
                .fileName(fileName)
                .fileType(fileType)
                .data(base64Data)
                .build();

        return ResponseEntity.ok(ApiResponse.success(fileInfo, "File Fetched successfully"));
    }
}
