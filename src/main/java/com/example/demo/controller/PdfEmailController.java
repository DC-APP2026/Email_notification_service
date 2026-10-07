package com.example.demo.controller;

import com.example.demo.service.EmailService;
import jakarta.mail.MessagingException;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/pdf")
public class PdfEmailController {

    private final EmailService emailService;

    public PdfEmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public String sendPdfEmail(
            @RequestParam String email,
            @RequestParam String name) {

        Map<String, Object> variables = new HashMap<>();

        variables.put("name", name);

        try {

            emailService.sendEmailWithAttachment(
                    email,
                    "Document Attached",
                    "pdf-email",
                    variables,
                    "attachments/sample.pdf"
            );

            return "PDF email sent successfully to " + email;

        } catch (MessagingException e) {

            return "Failed to send PDF email: " + e.getMessage();
        }
    }
}