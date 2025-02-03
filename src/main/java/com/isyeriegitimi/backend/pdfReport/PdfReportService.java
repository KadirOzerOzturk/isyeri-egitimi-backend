package com.isyeriegitimi.backend.pdfReport;

import aj.org.objectweb.asm.ClassWriter;
import com.isyeriegitimi.backend.model.Lecturer;
import com.isyeriegitimi.backend.model.Student;
import com.isyeriegitimi.backend.model.StudentGroup;
import com.isyeriegitimi.backend.model.WeeklyReport;
import com.isyeriegitimi.backend.repository.LecturerRepository;
import com.isyeriegitimi.backend.repository.StudentsInGroupRepository;
import com.isyeriegitimi.backend.repository.WeeklyReportRepository;
import com.isyeriegitimi.backend.repository.StudentRepository;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.font.FontProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import com.itextpdf.html2pdf.HtmlConverter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PdfReportService {

    @Autowired
    private WeeklyReportRepository weeklyReportRepository;
    @Autowired
    private StudentsInGroupRepository studentsInGroupRepository;
    @Autowired
    private StudentRepository studentRepository ;
    @Autowired
    private SpringTemplateEngine templateEngine;

    public ByteArrayOutputStream generateWeeklyReportPdf(UUID studentId) throws IOException {

        // Öğrenciye ait tüm haftalık raporları al
        List<WeeklyReport> reports = weeklyReportRepository.findByStudent_StudentId(studentId);
        StudentGroup studentGroup = studentsInGroupRepository.findByStudent_StudentId(studentId)
                .orElseThrow(() -> new RuntimeException("Student group not found"))
                .getStudentGroup();
        // Thymeleaf template context oluştur
        Context context = new Context();
        context.setVariable("reports", reports); // Haftalık raporlar listesini ekliyoruz
        context.setVariable("lecturer", studentGroup.getLecturer()); // Haftalık raporlar listesini ekliyoruz
        context.setVariable("student", reports.get(0).getStudent()); // Öğrenci bilgisi

        // HTML içeriğini Thymeleaf şablonundan oluştur
        String htmlContent = templateEngine.process("weeklyReport", context); // weeklyReport.html şablonunu render eder

        // HTML'yi PDF'ye dönüştür
        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(htmlContent, pdfOutputStream);

        return pdfOutputStream;
    }

    public ByteArrayOutputStream generateForm1ByStudentId(UUID studentId) {
        Optional<Student> student = studentRepository.findById(studentId);
        StudentGroup studentGroup = studentsInGroupRepository.findByStudent_StudentId(studentId)
                .orElseThrow(() -> new RuntimeException("Student group not found"))
                .getStudentGroup();
        Context context = new Context();

        context.setVariable("student", student.get());
        context.setVariable("lecturer", studentGroup.getLecturer());


        String htmlContent = templateEngine.process("form1", context);
        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(htmlContent, pdfOutputStream);


        return pdfOutputStream;
    }
    public  ByteArrayOutputStream generateForm2ByStudentId(UUID studentId) {

        Optional<Student> studentOptional = studentRepository.findById(studentId);
        if (studentOptional.isEmpty()) {
            throw new RuntimeException("Student not found with ID: " + studentId);
        }
        Student student = studentOptional.get();

        Context context = new Context();
        context.setVariable("student", student);
        context.setVariable("date", new SimpleDateFormat("dd.MM.yyyy").format(new Date()));
        String htmlContent = templateEngine.process("taahutname", context);

        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(htmlContent, pdfOutputStream);


        return pdfOutputStream;
    }
    public ByteArrayOutputStream generateForm3ByStudentId(UUID studentId) {
        Optional<Student> student = studentRepository.findById(studentId);
        StudentGroup studentGroup = studentsInGroupRepository.findByStudent_StudentId(studentId)
                .orElseThrow(() -> new RuntimeException("Student group not found"))
                .getStudentGroup();
        Context context = new Context();

        context.setVariable("student", student.get());
        context.setVariable("lecturer", studentGroup.getLecturer());


        String htmlContent = templateEngine.process("form3", context);
        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(htmlContent, pdfOutputStream);


        return pdfOutputStream;
    }

    public ByteArrayOutputStream generateForm4ByStudentId(UUID studentId) {
        Optional<Student> student = studentRepository.findById(studentId);
        StudentGroup studentGroup = studentsInGroupRepository.findByStudent_StudentId(studentId)
                .orElseThrow(() -> new RuntimeException("Student group not found"))
                .getStudentGroup();
        Context context = new Context();

        context.setVariable("student", student.get());
        context.setVariable("lecturer", studentGroup.getLecturer());


        String htmlContent = templateEngine.process("form4", context);
        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(htmlContent, pdfOutputStream);


        return pdfOutputStream;
    }
    public ByteArrayOutputStream generateForm4_1yStudentId(UUID studentId) {
        Optional<Student> student = studentRepository.findById(studentId);
        StudentGroup studentGroup = studentsInGroupRepository.findByStudent_StudentId(studentId)
                .orElseThrow(() -> new RuntimeException("Student group not found"))
                .getStudentGroup();
        Context context = new Context();

        context.setVariable("student", student.get());
        context.setVariable("lecturer", studentGroup.getLecturer());


        String htmlContent = templateEngine.process("form4.1", context);
        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(htmlContent, pdfOutputStream);


        return pdfOutputStream;
    }
}
