package com.isyeriegitimi.backend.pdfReport;

import aj.org.objectweb.asm.ClassWriter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.isyeriegitimi.backend.dto.FileInfoDto;
import com.isyeriegitimi.backend.model.*;
import com.isyeriegitimi.backend.repository.*;
import com.isyeriegitimi.backend.security.enums.Role;
import com.isyeriegitimi.backend.service.FileService;
import jakarta.transaction.Transactional;
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
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.Signature;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;


@Service
public class PdfReportService {


    private final FileInfoRepository fileInfoRepository;
    private WeeklyReportRepository weeklyReportRepository;
    private StudentsInGroupRepository studentsInGroupRepository;
    private StudentRepository studentRepository ;
    private FormAnswerRepository formAnswerRepository;
    private SurveyQuestionRepository surveyQuestionRepository;
    private SurveyAnswerRepository surveyAnswerRepository;
    private SpringTemplateEngine templateEngine;
    private FileService fileInfoService;
    private FormSignatureRepository formSignatureRepository;
    private FormRepository formRepository;

    public PdfReportService(WeeklyReportRepository weeklyReportRepository, StudentsInGroupRepository studentsInGroupRepository, StudentRepository studentRepository, FormAnswerRepository formAnswerRepository, SurveyQuestionRepository surveyQuestionRepository, SurveyAnswerRepository surveyAnswerRepository, SpringTemplateEngine templateEngine, FileService fileInfoService, FormSignatureRepository formSignatureRepository, FormRepository formRepository, FileInfoRepository fileInfoRepository) {
        this.weeklyReportRepository = weeklyReportRepository;
        this.studentsInGroupRepository = studentsInGroupRepository;
        this.studentRepository = studentRepository;
        this.formAnswerRepository = formAnswerRepository;
        this.surveyQuestionRepository = surveyQuestionRepository;
        this.surveyAnswerRepository = surveyAnswerRepository;
        this.templateEngine = templateEngine;
        this.fileInfoService = fileInfoService;
        this.formSignatureRepository = formSignatureRepository;
        this.formRepository = formRepository;
        this.fileInfoRepository = fileInfoRepository;
    }
    private static QrInfo generateQR(String code) throws Exception {

        int imageSize = 200;
        BitMatrix matrix = new MultiFormatWriter().encode(code, BarcodeFormat.QR_CODE,
                imageSize, imageSize);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "png", bos);
        String image = Base64.getEncoder().encodeToString(bos.toByteArray());

        QrInfo qrInfo = new QrInfo();
        qrInfo.setCode(code);
        qrInfo.setImage(image);
        return qrInfo;
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

    private String convertToVariableName(String text) {
        return text.toLowerCase()
                .replace("ı", "i").replace("İ", "I")
                .replace("ğ", "g").replace("Ğ", "G")
                .replace("ü", "u").replace("Ü", "U")
                .replace("ş", "s").replace("Ş", "S")
                .replace("ö", "o").replace("Ö", "O")
                .replace("ç", "c").replace("Ç", "C")
                .replaceAll("[^a-zA-Z0-9]", ""); // Sadece harf ve rakamları bırak
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

    @Transactional
    public ByteArrayOutputStream generateForm1ByStudentId(UUID formId, UUID studentId) throws Exception {
        Optional<Student> student = studentRepository.findById(studentId);

        List<FormAnswer> formAnswers = formAnswerRepository.findByStudentIdAndFormId(studentId, formId);
        List<FormSignature> signatures = formSignatureRepository.findAllByFormIdAndStudentId(formId, studentId);
        FileInfoDto studentPhoto = fileInfoService.getFile(studentId,"STUDENT","profilePhoto");
        String base64img = "data:" + studentPhoto.getFileType() + ";base64," + studentPhoto.getData();
        QrInfo qr = generateQR(generateUnique12DigitNumber());

        Context context = new Context();
        context.setVariable("qr", "data:image/png;base64," + qr.getImage());
        context.setVariable("student", student.orElseThrow(() -> new RuntimeException("Öğrenci bulunamadı")));
        context.setVariable("answers", formAnswers);
        context.setVariable("signatures", signatures);
        context.setVariable("studentPhoto",base64img);

        for (FormAnswer answer : formAnswers) {
            String questionText = answer.getFormQuestion().getQuestionText();
            String answerValue = answer.getAnswer();
            String variableName = convertToVariableName(questionText);

            System.out.println("Variable Name: " + variableName + " = " + answerValue);
            context.setVariable(variableName, answerValue);
        }

        String htmlContent = templateEngine.process("kabulFormu", context);
        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(htmlContent, pdfOutputStream);

        String encodedPdfData = Base64.getEncoder().encodeToString(pdfOutputStream.toByteArray());

        FileInfo fileInfo = FileInfo.builder()
                .fileName("form1" )
                .fileType("application/pdf")
                .ownerId(studentId)
                .ownerRole(Role.STUDENT.toString())
                .data(encodedPdfData)
                .uploadDate(new Date())
                .barcodeNumber(qr.getCode())
                .build();
        fileInfoService.deleteFileIfExists(studentId,"STUDENT","form1");
        fileInfoService.uploadFile(fileInfo);
        return pdfOutputStream;
    }

    public ByteArrayOutputStream generateForm2ByStudentId(UUID studentId, UUID formId) throws Exception {
        Optional<Student> studentOptional = studentRepository.findById(studentId);
        if (studentOptional.isEmpty()) {
            throw new RuntimeException("Student not found with ID: " + studentId);
        }

        List<FormAnswer> formAnswers = formAnswerRepository.findByStudentIdAndFormId(studentId, formId);
        Student student = studentOptional.get();
        List<FormSignature> signatures = formSignatureRepository.findAllByFormIdAndStudentId(formId, studentId);

        String startingDate = null;
        String duration = null;
        String companyName = null;
        String insurance = null;

        for (FormAnswer answer : formAnswers) {
            String question = answer.getFormQuestion().getQuestionText();
            switch (question) {
                case "Başlangıç Tarihi":
                    startingDate = answer.getAnswer();
                    break;
                case "İşyeri Eğitimi Yapılacak Süre (HAFTA)":
                    duration = answer.getAnswer();
                    break;
                case "İşyeri Eğitimi Yapılacak Firmanın Adı":
                    System.out.println("company "+ answer.getAnswer());
                    companyName = answer.getAnswer();
                    break;
                case "Ailemden, annem/babam üzerinden veya kamu/özel sektörde çalışmamdan dolayı genel sağlık sigortası kapsamında sağlık hizmeti alıyorum.":
                    if ("Evet".equalsIgnoreCase(answer.getAnswer())) {
                        insurance = "true";
                    }
                    break;
                case "Ailemden, annem/babam üzerinden, genel sağlık sigortası kapsamında sağlık hizmeti almıyorum.":
                    if ("Evet".equalsIgnoreCase(answer.getAnswer())) {
                        insurance = "false";
                    }
                    break;
            }
        }

        QrInfo qr = generateQR(generateUnique12DigitNumber());

        Context context = new Context();
        context.setVariable("qr", "data:image/png;base64," + qr.getImage());
        context.setVariable("student", student);
        context.setVariable("date", new SimpleDateFormat("dd.MM.yyyy").format(new Date()));
        context.setVariable("startingDate", startingDate);
        context.setVariable("duration", duration);
        context.setVariable("companyName", companyName);
        context.setVariable("insurance", insurance);
        context.setVariable("signatures",signatures);
        String htmlContent = templateEngine.process("taahutname", context);

        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(htmlContent, pdfOutputStream);
        String encodedPdfData = Base64.getEncoder().encodeToString(pdfOutputStream.toByteArray());

        FileInfo fileInfo = FileInfo.builder()
                .fileName("form2" )
                .fileType("application/pdf")
                .ownerId(studentId)
                .ownerRole(Role.STUDENT.toString())
                .data(encodedPdfData)
                .uploadDate(new Date())
                .barcodeNumber(qr.getCode())
                .build();
        fileInfoService.deleteFileIfExists(studentId,"STUDENT","form2");
        fileInfoService.uploadFile(fileInfo);
        return pdfOutputStream;
    }

    public ByteArrayOutputStream generateForm3ByStudentId(UUID formId, UUID studentId) throws Exception {
        Optional<Student> student = studentRepository.findById(studentId);
        StudentGroup studentGroup = studentsInGroupRepository.findByStudent_StudentId(studentId)
                .orElseThrow(() -> new RuntimeException("Student group not found"))
                .getStudentGroup();
        List<FormAnswer> formAnswers = formAnswerRepository.findByStudentIdAndFormId(studentId, formId);
        List<FormSignature> signatures = formSignatureRepository.findAllByFormIdAndStudentId(formId, studentId);

        QrInfo qr = generateQR(generateUnique12DigitNumber());

        Context context = new Context();
        context.setVariable("qr", "data:image/png;base64," + qr.getImage());
        String startingDate = null;
        String departments = null;
        String visitDate = null;
        String mentorImpression= null;
        String lecturerImpression= null;
        for (FormAnswer answer : formAnswers) {
            String question = answer.getFormQuestion().getQuestionText();
            switch (question) {
                case "İşyeri Eğitimine Başlama Tarihi":
                    startingDate = answer.getAnswer();
                    break;
                case "İşyerinde Çalıştığı Bölümler":
                    departments = answer.getAnswer();
                    break;
                case "İşyeri Ziyaret Edilen Tarih":
                    visitDate = answer.getAnswer();
                    break;
                case "İŞYERİ YETKİLİSİ/GÖREVLİSİNİN ÖĞRENCİ HAKKINDAKİ İZLENİMLERİ":

                    mentorImpression = answer.getAnswer();

                    break;
                case "İZLEYİCİ ÖĞRETİM ÜYESİNİN ÖĞRENCİ HAKKINDAKİ İZLENİMLERİ":

                    lecturerImpression =answer.getAnswer();

                    break;
            }

        }



        context.setVariable("student", student.get());
        context.setVariable("lecturer", studentGroup.getLecturer());
        context.setVariable("startingDate", startingDate);
        context.setVariable("departments", departments);
        context.setVariable("visitDate", visitDate);
        context.setVariable("mentorImpression", mentorImpression);
        context.setVariable("lecturerImpression", lecturerImpression);
        context.setVariable("signatures",signatures);
        context.setVariable("mentor",student.get().getMentor());

        String htmlContent = templateEngine.process("form3", context);
        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(htmlContent, pdfOutputStream);
        String encodedPdfData = Base64.getEncoder().encodeToString(pdfOutputStream.toByteArray());

        FileInfo fileInfo = FileInfo.builder()
                .fileName("form3" )
                .fileType("application/pdf")
                .ownerId(studentId)
                .ownerRole(Role.STUDENT.toString())
                .data(encodedPdfData)
                .uploadDate(new Date())
                .barcodeNumber(qr.getCode())
                .build();
        fileInfoService.deleteFileIfExists(studentId,"STUDENT","form3");
        fileInfoService.uploadFile(fileInfo);
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