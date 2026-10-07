package com.example.demo.controller;

import com.example.demo.model.Trainee;
import com.example.demo.service.EmailService;
import com.example.demo.service.ExcelService;
import com.example.demo.service.OtpService;

import jakarta.mail.MessagingException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/otp")
public class OtpController {

    private final OtpService otpService;
    private final EmailService emailService;
    private final ExcelService excelService;

    public OtpController(
            OtpService otpService,
            EmailService emailService,
            ExcelService excelService) {

        this.otpService = otpService;
        this.emailService = emailService;
        this.excelService = excelService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendOtp(
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

            // Get trainee email
            String email = trainee.getEmail();

            // Generate OTP
            String otp =
                    otpService.generateOtp();

            // Create template variables
            Map<String, Object> variables =
                    new HashMap<>();

            variables.put("otp", otp);

            // Send OTP email
            emailService.sendEmail(
                    email,
                    "OTP Verification",
                    "otp-email",
                    variables
            );

            return ResponseEntity.ok(
                    "OTP sent successfully to "
                    + email
            );

        } catch (MessagingException e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            "Failed to send OTP: "
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