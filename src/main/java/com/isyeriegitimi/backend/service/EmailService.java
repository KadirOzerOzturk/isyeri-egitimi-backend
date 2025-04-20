package com.isyeriegitimi.backend.service;

import com.isyeriegitimi.backend.dto.FeedbackDto;
import com.isyeriegitimi.backend.exceptions.InternalServerErrorException;
import com.isyeriegitimi.backend.model.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.io.File;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Autowired
    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendEmail(Email email) throws MessagingException {
        try {

            Context context = new Context();
            context.setVariable("name", email.getName());
            context.setVariable("message", email.getMessage());
            context.setVariable("subject", email.getSubject());
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            String htmlContent = templateEngine.process("emailTemplate", context);
            helper.setTo(email.getTo());
            helper.setFrom("gaziisyeri@gmail.com");
            helper.setSubject(email.getSubject());
            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new InternalServerErrorException("Failed to send email"+ e.getMessage());
        }
    }
    public void sendFeedbackEmail(FeedbackDto feedback) throws MessagingException {
        try {
            Context context = new Context();
            context.setVariable("name", feedback.getName());
            context.setVariable("surname", feedback.getSurname());
            context.setVariable("email", feedback.getEmail());
            context.setVariable("subject", feedback.getSubject());
            context.setVariable("message", feedback.getMessage());


            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);


            String htmlContent = templateEngine.process("feedbackTemplate", context);

            helper.setTo("gaziisyeri@gmail.com");
            helper.setFrom(feedback.getEmail());
            helper.setSubject("Geri Bildirim - " + feedback.getSubject());
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new InternalServerErrorException("Failed to send feedback email: " + e.getMessage());
        }
    }
}
