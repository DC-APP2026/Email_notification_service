package com.example.demo.controller;

import com.example.demo.model.Trainee;
import com.example.demo.service.EmailService;
import com.example.demo.service.ExcelService;

import jakarta.mail.MessagingException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/welcome")
public class WelcomeController {

    private final EmailService emailService;
    private final ExcelService excelService;

    public WelcomeController(
            EmailService emailService,
            ExcelService excelService) {

        this.emailService = emailService;
        this.excelService = excelService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendWelcomeEmail(
            @RequestParam String traineeId) {

        try {

            // Find trainee from Excel
            Trainee trainee =
                    excelService.findTraineeById(traineeId);

            if (trainee == null) {

                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(
                                "Trainee not found: "
                                + traineeId
                        );
            }

            // Get trainee information
            String email = trainee.getEmail();
            String name = trainee.getTraineeName();

            // Template variables
            Map<String, Object> variables =
                    new HashMap<>();

            variables.put("name", name);

            // Send welcome email
            emailService.sendEmail(
                    email,
                    "Welcome to Our Application",
                    "welcome-email",
                    variables
            );

            return ResponseEntity.ok(
                    "Welcome email sent successfully to "
                    + email
            );

        } catch (MessagingException e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            "Failed to send welcome email: "
                            + e.getMessage()
                    );

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            "Error processing trainee: "
                            + e.getMessage()
                    );
        }
    }
}