package com.isyeriegitimi.backend.pdfReport;

import com.isyeriegitimi.backend.model.DownloadFormRequest;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;


@RestController
@RequestMapping("/pdf")
public class PdfReportController {

    @Autowired
    private PdfReportService pdfReportService;


    @GetMapping("/download/weekly-report/{studentId}")
    public ResponseEntity<byte[]> downloadWeeklyReports(@PathVariable UUID studentId) throws IOException {
        byte[] pdfContents = pdfReportService.generateWeeklyReportPdf(studentId).toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=weekly_reports.pdf");
        headers.add("Content-Type", "application/pdf");
        return new ResponseEntity<>(pdfContents, headers, HttpStatus.OK);
    }
    @GetMapping("/download/form1/{studentId}/{formId}")
    public ResponseEntity<byte[]> downloadForm1(@PathVariable UUID studentId,@PathVariable UUID formId) throws Exception {
        byte[] pdfContents = pdfReportService.generateForm1ByStudentId(formId,studentId).toByteArray();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=form1.pdf");
        headers.add("Content-Type", "application/pdf");

        return new ResponseEntity<>(pdfContents, headers, HttpStatus.OK);
    }
    @GetMapping("/download/form2/{studentId}")
    public ResponseEntity<byte[]> downloadForm2(@PathVariable UUID studentId) throws IOException {
        byte[] pdfContents = pdfReportService.generateForm2ByStudentId(studentId).toByteArray();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=taahutname.pdf");
        headers.add("Content-Type", "application/pdf");

        return new ResponseEntity<>(pdfContents, headers, HttpStatus.OK);
    }
    @GetMapping("/download/form3/{studentId}")
    public ResponseEntity<byte[]> downloadForm3(@PathVariable UUID studentId) throws IOException {
        byte[] pdfContents = pdfReportService.generateForm3ByStudentId(studentId).toByteArray();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=form3.pdf");
        headers.add("Content-Type", "application/pdf");

        return new ResponseEntity<>(pdfContents, headers, HttpStatus.OK);
    }
    @GetMapping("/download/form4/{studentId}")
    public ResponseEntity<byte[]> downloadForm4(@PathVariable UUID studentId) throws IOException {
        byte[] pdfContents = pdfReportService.generateForm4ByStudentId(studentId).toByteArray();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=form4.pdf");
        headers.add("Content-Type", "application/pdf");

        return new ResponseEntity<>(pdfContents, headers, HttpStatus.OK);
    }
    @GetMapping("/download/form4.1/{studentId}")
    public ResponseEntity<byte[]> downloadForm4_1(@PathVariable UUID studentId) throws IOException {
        byte[] pdfContents = pdfReportService.generateForm4_1yStudentId(studentId).toByteArray();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=form4.1.pdf");
        headers.add("Content-Type", "application/pdf");

        return new ResponseEntity<>(pdfContents, headers, HttpStatus.OK);
    }
    @GetMapping("/download/survey/{surveyId}/student/{studentId}")
    public ResponseEntity<byte[]> downloadStudentSurvey(@PathVariable UUID studentId,@PathVariable UUID surveyId) throws Exception {
        byte[] pdfContents = pdfReportService.generateSurveyByStudentId(studentId,surveyId).toByteArray();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=degerlendirmeAnketi.pdf");
        headers.add("Content-Type", "application/pdf");

        return new ResponseEntity<>(pdfContents, headers, HttpStatus.OK);
    }
}
