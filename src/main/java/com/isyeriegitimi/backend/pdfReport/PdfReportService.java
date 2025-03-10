package com.isyeriegitimi.backend.pdfReport;

import aj.org.objectweb.asm.ClassWriter;
import com.isyeriegitimi.backend.model.*;
import com.isyeriegitimi.backend.repository.*;
import com.isyeriegitimi.backend.security.enums.Role;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import com.itextpdf.html2pdf.HtmlConverter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;


@Service
public class PdfReportService {

    
    private WeeklyReportRepository weeklyReportRepository;
    private StudentsInGroupRepository studentsInGroupRepository;
    private StudentRepository studentRepository ;
    private FormAnswerRepository formAnswerRepository;
    private SurveyQuestionRepository surveyQuestionRepository;
    private SurveyAnswerRepository surveyAnswerRepository;
    private SpringTemplateEngine templateEngine;
    private FileInfoRepository fileInfoRepository;
    private FormSignatureRepository formSignatureRepository;
    private FormRepository formRepository;

    public PdfReportService(WeeklyReportRepository weeklyReportRepository, StudentsInGroupRepository studentsInGroupRepository, StudentRepository studentRepository, FormAnswerRepository formAnswerRepository, SurveyQuestionRepository surveyQuestionRepository, SurveyAnswerRepository surveyAnswerRepository, SpringTemplateEngine templateEngine, FileInfoRepository fileInfoRepository, FormSignatureRepository formSignatureRepository, FormRepository formRepository) {
        this.weeklyReportRepository = weeklyReportRepository;
        this.studentsInGroupRepository = studentsInGroupRepository;
        this.studentRepository = studentRepository;
        this.formAnswerRepository = formAnswerRepository;
        this.surveyQuestionRepository = surveyQuestionRepository;
        this.surveyAnswerRepository = surveyAnswerRepository;
        this.templateEngine = templateEngine;
        this.fileInfoRepository = fileInfoRepository;
        this.formSignatureRepository = formSignatureRepository;
        this.formRepository = formRepository;
    }

    private static BufferedImage generateEAN13BarcodeImage() throws Exception {
        String uniqueBarcodeNumber = generateUnique12DigitNumber();
        Barcode barcode = BarcodeFactory.createEAN13(uniqueBarcodeNumber);
        barcode.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        return BarcodeImageHandler.getImage(barcode);
    }
    private static String generateUnique12DigitNumber() {
        UUID uuid = UUID.randomUUID();
        BigInteger bigInt = new BigInteger(uuid.toString().replaceAll("-", ""), 16);
        BigInteger mod = new BigInteger("1000000000000");
        BigInteger uniqueNumber = bigInt.mod(mod);
        return String.format("%012d", uniqueNumber);
    }
    public String convertBufferedImageToBase64(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.getEncoder().encodeToString(imageBytes);
    }
    public ByteArrayOutputStream generateWeeklyReportPdf(UUID studentId) throws IOException {

        List<WeeklyReport> reports = weeklyReportRepository.findByStudent_StudentId(studentId);
        StudentGroup studentGroup = studentsInGroupRepository.findByStudent_StudentId(studentId)
                .orElseThrow(() -> new RuntimeException("Student group not found"))
                .getStudentGroup();

        Context context = new Context();
        context.setVariable("reports", reports);
        if( studentGroup.getLecturer() == null) {
            context.setVariable("lecturer",null);
        }else {
            context.setVariable("lecturer", studentGroup.getLecturer());
        }
        context.setVariable("student", reports.get(0).getStudent());

        String htmlContent = templateEngine.process("weeklyReport", context); // weeklyReport.html şablonunu render eder

        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(htmlContent, pdfOutputStream);

        return pdfOutputStream;
    }

    public ByteArrayOutputStream generateForm1ByStudentId(DownloadFormRequest downloadFormRequest) throws Exception {
        Optional<Student> student = studentRepository.findById(downloadFormRequest.getFormId());
        StudentGroup studentGroup = studentsInGroupRepository.findByStudent_StudentId(downloadFormRequest.getFormId())
                .orElseThrow(() -> new RuntimeException("Student group not found"))
                .getStudentGroup();
        List<FormAnswer> formAnswers = formAnswerRepository.findByUserIdAndUserRole(downloadFormRequest.getUserId(), downloadFormRequest.getUserRole());

        // Corrected the method call
        List<FormSignature> formSignatures = formSignatureRepository.findAllByFormIdAndSignedByAndSignedByRole(downloadFormRequest.getFormId() , downloadFormRequest.getUserId(),Role.valueOf(downloadFormRequest.getUserRole()));

        BufferedImage barcodeImage = generateEAN13BarcodeImage();
        String barcodeBase64 = convertBufferedImageToBase64(barcodeImage);

        Context context = new Context();
        context.setVariable("barcode", "data:image/png;base64," + barcodeBase64);
        context.setVariable("student", student.get());
        context.setVariable("lecturer", studentGroup.getLecturer());
        context.setVariable("answers", formAnswers);
        context.setVariable("signatures", formSignatures);

        String htmlContent = templateEngine.process("kabulFormu", context);
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
        List<FormAnswer> formAnswers = formAnswerRepository.findByUserIdAndUserRole(studentId, "STUDENT");
        List<FormSignature> formSignatures = formSignatureRepository.findAllByFormIdAndSignedByAndSignedByRole(UUID.fromString("0f7ba07d-1d21-40c3-9b3e-dc7f823d22e9"),student.get().getCompany().getCompanyId(), Role.valueOf("COMPANY"));
        System.out.println("Form Signatures: " + formSignatures);
        Context context = new Context();
        context.setVariable("signatures", formSignatures);
        context.setVariable("student", student.get());
        context.setVariable("lecturer", studentGroup.getLecturer());
        context.setVariable("answers", formAnswers);


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

    public ByteArrayOutputStream generateSurveyByStudentId(UUID studentId,UUID surveyId) throws Exception {
        Optional<Student> student = studentRepository.findById(studentId);
        List<SurveyAnswer> surveyAnswers = surveyAnswerRepository.findBySurveyIdAndUserId(surveyId, studentId);
        System.out.println("Survey Answers: " + surveyAnswers);
        List<SurveyQuestion> surveyQuestions = surveyQuestionRepository.findBySurvey_Id(surveyAnswers.get(0).getSurvey().getId()); ;
        BufferedImage barcodeImage = generateEAN13BarcodeImage();
        String barcodeBase64 = convertBufferedImageToBase64(barcodeImage);
        Context context = new Context();
        context.setVariable("barcode", "data:image/png;base64," + barcodeBase64);
        context.setVariable("student", student.get());
        context.setVariable("answers", surveyAnswers);
        context.setVariable("questions", surveyQuestions);

        String htmlContent = templateEngine.process("anket", context);
        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(htmlContent, pdfOutputStream);
        System.out.println("HTML Content: " + htmlContent);

        return pdfOutputStream;
    }
}
