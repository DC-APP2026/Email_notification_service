package com.example.demo.controller;

import com.example.demo.model.Trainee;
import com.example.demo.service.DocumentService;
import com.example.demo.service.EmailService;
import com.example.demo.service.ExcelService;

import jakarta.mail.MessagingException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/certificates")
public class CertificateController {

    private final ExcelService excelService;
    private final DocumentService documentService;
    private final EmailService emailService;

    public CertificateController(
            ExcelService excelService,
            DocumentService documentService,
            EmailService emailService) {

        this.excelService = excelService;
        this.documentService = documentService;
        this.emailService = emailService;
    }

    // Generate certificates for ALL trainees
    @PostMapping("/generate")
    public ResponseEntity<?> generateAllCertificates() {

        try {

            List<Trainee> trainees =
                    excelService.readTrainees();

            List<String> generatedFiles =
                    new ArrayList<>();

            for (Trainee trainee : trainees) {

                Path file =
                        documentService.generateCertificate(
                                trainee
                        );

                generatedFiles.add(
                        file.toString()
                );
            }

            return ResponseEntity.ok(
                    generatedFiles
            );

        } catch (Exception e) {

            return ResponseEntity
                    .internalServerError()
                    .body(
                            "Certificate generation failed: "
                                    + e.getMessage()
                    );
        }
    }


    // Generate certificate AND email it to ONE trainee
    @PostMapping("/generate/{traineeId}")
    public ResponseEntity<?> generateSingleCertificate(
            @PathVariable String traineeId) {

        try {

            List<Trainee> trainees =
                    excelService.readTrainees();

            for (Trainee trainee : trainees) {

                if (trainee.getTraineeId()
                        .equalsIgnoreCase(traineeId)) {

                    // --------------------------------
                    // 1. Generate certificate
                    // --------------------------------

                    Path file =
                            documentService
                                    .generateCertificate(
                                            trainee
                                    );


                    // --------------------------------
                    // 2. Prepare email variables
                    // --------------------------------

                    Map<String, Object> variables =
                            new HashMap<>();

                    variables.put(
                            "traineeName",
                            trainee.getTraineeName()
                    );

                    variables.put(
                            "courseName",
                            trainee.getCourse()
                    );


                    // --------------------------------
                    // 3. Send certificate by email
                    // --------------------------------

                    emailService.sendEmailWithAttachment(
                            trainee.getEmail(),
                            "Training Certificate",
                            "certificate-email",
                            variables,
                            file
                    );


                    // --------------------------------
                    // 4. Return success response
                    // --------------------------------

                    return ResponseEntity.ok(
                            "Certificate generated and "
                                    + "emailed successfully to "
                                    + trainee.getEmail()
                    );
                }
            }

            // Trainee ID was not found
            return ResponseEntity
                    .status(404)
                    .body(
                            "Trainee not found: "
                                    + traineeId
                    );

        } catch (MessagingException e) {

            return ResponseEntity
                    .internalServerError()
                    .body(
                            "Certificate generated, "
                                    + "but email failed: "
                                    + e.getMessage()
                    );

        } catch (Exception e) {

            return ResponseEntity
                    .internalServerError()
                    .body(
                            "Certificate generation failed: "
                                    + e.getMessage()
                    );
        }
    }
}