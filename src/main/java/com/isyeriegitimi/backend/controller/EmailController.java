package com.isyeriegitimi.backend.controller;

import com.isyeriegitimi.backend.model.ApiResponse;
import com.isyeriegitimi.backend.model.Email;
import com.isyeriegitimi.backend.service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;

@RestController
@RequestMapping("/emails")
public class EmailController {

    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public ResponseEntity<ApiResponse<?>> sendEmail(@RequestBody Email email) throws MessagingException {

        emailService.sendEmail(email);
        return ResponseEntity.ok(ApiResponse.success(null, "Email sent successfully"));

    }
}
