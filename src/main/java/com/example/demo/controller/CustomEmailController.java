package com.example.demo.controller;

import com.example.demo.dto.CustomEmailRequest;
import com.example.demo.model.Trainee;
import com.example.demo.service.EmailService;
import com.example.demo.service.ExcelService;
import com.example.demo.service.TemplateService;

import jakarta.mail.MessagingException;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
@CrossOrigin(origins = "*")
public class CustomEmailController {

    private final EmailService emailService;
    private final ExcelService excelService;
    private final TemplateService templateService;

    public CustomEmailController(
            EmailService emailService,
            ExcelService excelService,
            TemplateService templateService) {

        this.emailService = emailService;
        this.excelService = excelService;
        this.templateService = templateService;
    }

    @PostMapping("/custom")
    public String sendCustomEmail(
            @RequestBody CustomEmailRequest request) {

        try {

            // Find trainee from Excel
            Trainee trainee =
                    excelService.findTraineeById(
                            request.getTraineeId()
                    );

            if (trainee == null) {

                return "Trainee not found: "
                        + request.getTraineeId();
            }


            // Replace placeholders
            String finalMessage =
                    templateService.replacePlaceholders(
                            request.getMessage(),
                            trainee
                    );


            // Send email
            emailService.sendCustomEmail(
                    trainee.getEmail(),
                    request.getSubject(),
                    finalMessage
            );


            return "Custom email sent successfully to "
                    + trainee.getEmail();

        } catch (MessagingException e) {

            return "Failed to send email: "
                    + e.getMessage();

        } catch (Exception e) {

            return "Error processing trainee data: "
                    + e.getMessage();
        }
    }
}