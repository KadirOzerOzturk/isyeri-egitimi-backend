package com.isyeriegitimi.backend.controller;

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
    public ResponseEntity<?> createPreApplication(@RequestBody PreApplication entry) {
        for (int i=0;i<100;i++){
            service.savePreApplication(entry);
        }
        return ResponseEntity.ok("Basarili");
    }

    @GetMapping
    public ResponseEntity<List<PreApplication>> getAllFormEntries() {
        return ResponseEntity.ok(service.getAllFormEntries());
    }

//    @GetMapping("/download-pdf")
//    public ResponseEntity<InputStreamResource> downloadPdf() {
//        List<PreApplication> applications = service.getAllFormEntries();
//        ByteArrayInputStream bis = generatePdf(applications);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Disposition", "inline; filename=applications.pdf");
//
//        return ResponseEntity.ok()
//                .headers(headers)
//                .contentType(MediaType.APPLICATION_PDF)
//                .body(new InputStreamResource(bis));
//    }
//    private ByteArrayInputStream generatePdf(List<PreApplication> applications) {
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//
//        PdfDocument pdf = new PdfDocument(new PdfWriter(out));
//        Document document = new Document(pdf, PageSize.A4.rotate());
//
//        float[] columnWidths = {1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2};
//        Table table = new Table(UnitValue.createPercentArray(columnWidths));
//        table.setWidth(UnitValue.createPercentValue(100));
//
//        table.addHeaderCell(new Cell().add(new Paragraph("No")));
//        table.addHeaderCell(new Cell().add(new Paragraph("Ad Soyad")));
//        table.addHeaderCell(new Cell().add(new Paragraph("TC")));
//        table.addHeaderCell(new Cell().add(new Paragraph("Eposta")));
//        table.addHeaderCell(new Cell().add(new Paragraph("Telefon")));
//        table.addHeaderCell(new Cell().add(new Paragraph("AGNO")));
//        table.addHeaderCell(new Cell().add(new Paragraph("Tercih Edilen Dönem")));
//        table.addHeaderCell(new Cell().add(new Paragraph("Başarısız Dersler")));
//        table.addHeaderCell(new Cell().add(new Paragraph("Şirket Bilgisi")));
//        table.addHeaderCell(new Cell().add(new Paragraph("Protokol Durumu")));
//        table.addHeaderCell(new Cell().add(new Paragraph("Zorunlu Staj Gün Sayısı")));
//        table.addHeaderCell(new Cell().add(new Paragraph("Firmada Staj Yapma İsteği")));
//        table.addHeaderCell(new Cell().add(new Paragraph("Özel Durumlar")));
//
//        for (PreApplication application : applications) {
//            table.addCell(new Cell().add(new Paragraph(nonNull(application.getOgrenciNumarasi()))));
//            table.addCell(new Cell().add(new Paragraph(nonNull(application.getAdSoyad()))));
//            table.addCell(new Cell().add(new Paragraph(nonNull(application.getTckNumarasi()))));
//            table.addCell(new Cell().add(new Paragraph(nonNull(application.getEposta()))));
//            table.addCell(new Cell().add(new Paragraph(nonNull(application.getTelefon()))));
//            table.addCell(new Cell().add(new Paragraph(nonNull(application.getGenelNotOrtalamasi()))));
//            table.addCell(new Cell().add(new Paragraph(nonNull(application.getTercihEdilenDonem()))));
//            table.addCell(new Cell().add(new Paragraph(nonNull(application.getBasarisizDersler()))));
//            table.addCell(new Cell().add(new Paragraph(nonNull(application.getSirketBilgisi()))));
//            table.addCell(new Cell().add(new Paragraph(nonNull(application.getProtokolDurumu()))));
//            table.addCell(new Cell().add(new Paragraph(nonNull(application.getZorunluStajGunSayisi()))));
//            table.addCell(new Cell().add(new Paragraph(nonNull(application.getFirmadaStajYapmaIstegi()))));
//            table.addCell(new Cell().add(new Paragraph(nonNull(application.getOzelDurumlar()))));
//        }
//
//        document.add(table);
//        document.close();
//
//        return new ByteArrayInputStream(out.toByteArray());
//    }
//
//    private String nonNull(String value) {
//        return value == null ? "" : value;
//    }


    @GetMapping("/download-excel")
    public ResponseEntity<Resource> downloadExcel() {
        List<PreApplication> applications = service.getAllFormEntries();

        // Excel dosyasını oluştur
        writeToExcel(applications);

        // Dosyayı diske yazdıktan sonra indirme işlemi
        Resource resource = new FileSystemResource("PreApplications.xlsx");

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"PreApplications.xlsx\"")
                .body(resource);
    }

    public void writeToExcel(List<PreApplication> applications) {
        // Workbook oluştur
        Workbook workbook = new XSSFWorkbook();

        // Sayfa oluştur
        Sheet sheet = workbook.createSheet("Ön Başvuru Formları");

        // Başlık satırı oluştur
        Row headerRow = sheet.createRow(0);
        String[] columns = {"Öğrenci No", "Ad Soyad", "TCK Numarası", "Eposta", "Telefon",
                "Genel Not Ortalaması", "Tercih Edilen Dönem", "Başarısız Dersler",
                "Şirket Bilgisi", "Protokol Durumu", "Zorunlu Staj Gün Sayısı",
                "Firmada Staj Yapma İsteği", "Özel Durumlar"};

        // Başlık hücrelerini ekle
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }

        // Verileri eklemek için döngü
        int rowNum = 1;
        for (PreApplication application : applications) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(application.getOgrenciNumarasi());
            row.createCell(1).setCellValue(application.getAdSoyad());
            row.createCell(2).setCellValue(application.getTckNumarasi());
            row.createCell(3).setCellValue(application.getEposta());
            row.createCell(4).setCellValue(application.getTelefon());
            row.createCell(5).setCellValue(application.getGenelNotOrtalamasi());
            row.createCell(6).setCellValue(application.getTercihEdilenDonem());
            row.createCell(7).setCellValue(application.getBasarisizDersler());
            row.createCell(8).setCellValue(application.getSirketBilgisi());
            row.createCell(9).setCellValue(application.getProtokolDurumu());
            row.createCell(10).setCellValue(application.getZorunluStajGunSayisi());
            row.createCell(11).setCellValue(application.getFirmadaStajYapmaIstegi());
            row.createCell(12).setCellValue(application.getOzelDurumlar());
        }

        // Dosyayı diske yaz
        try (FileOutputStream fileOut = new FileOutputStream("PreApplications.xlsx")) {
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Workbook kapat
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
