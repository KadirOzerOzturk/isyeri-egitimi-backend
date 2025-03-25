//package com.isyeriegitimi.backend.controller;
//
//import com.isyeriegitimi.backend.model.Email;
//import com.isyeriegitimi.backend.service.EmailService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.thymeleaf.context.Context;
//
//@RestController
//@RequestMapping("/emails")
//public class EmailController {
//
//    private final EmailService emailService;
//
//    @Autowired
//    public EmailController(EmailService emailService) {
//        this.emailService = emailService;
//    }
//
//    @PostMapping("/send")
//    public String sendEmail(@RequestBody Email email) {
//        Context context = new Context();
//        context.setVariable("name", email.getName());
//        context.setVariable("message", email.getMessage());
//        context.setVariable("subject", email.getSubject());
//        try {
//            emailService.sendEmail(email.getTo(), email.getSubject(), "emailTemplate", context);
//            return "Email sent successfully!";
//        } catch (Exception e) {
//            return "Error sending email: " + e.getMessage();
//        }
//    }
//}
