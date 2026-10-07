package com.example.demo.service;

import com.example.demo.model.Trainee;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Service
public class DocumentService {

    public Path generateCertificate(Trainee trainee) throws Exception {

        // Load Word template
        ClassPathResource resource =
                new ClassPathResource("templates/tmp.docx");

        try (InputStream inputStream = resource.getInputStream();
             XWPFDocument document =
                     new XWPFDocument(inputStream)) {

            // Create placeholder-value map
            Map<String, String> replacements =
                    new HashMap<>();

            replacements.put(
                    "{{traineeName}}",
                    trainee.getTraineeName()
            );

            replacements.put(
                    "{{traineeId}}",
                    trainee.getTraineeId()
            );

            replacements.put(
                    "{{courseName}}",
                    trainee.getCourse()
            );

            replacements.put(
                    "{{trainerName}}",
                    trainee.getTrainerName()
            );

            replacements.put(
                    "{{trainerId}}",
                    trainee.getTrainerId()
            );

            replacements.put(
                    "{{duration}}",
                    trainee.getDuration()
            );

            replacements.put(
                    "{{startDate}}",
                    trainee.getStartDate()
            );

            replacements.put(
                    "{{completionDate}}",
                    trainee.getCompletionDate()
            );

            // Generate certificate ID
            replacements.put(
                    "{{certificateId}}",
                    "CERT-2026-" + trainee.getTraineeId()
            );

            replacements.put(
                    "{{adminName}}",
                    trainee.getAdminName()
            );

            // Replace placeholders
            replacePlaceholders(
                    document,
                    replacements
            );

            // Create output directory
            Path outputDirectory =
                    Path.of("generated-certificates");

            Files.createDirectories(
                    outputDirectory
            );

            // Create output file
            Path outputFile =
                    outputDirectory.resolve(
                            trainee.getTraineeId()
                                    + "_Certificate.docx"
                    );

            // Save generated Word document
            try (OutputStream outputStream =
                         Files.newOutputStream(outputFile)) {

                document.write(outputStream);
            }

            System.out.println(
                    "Certificate generated: "
                            + outputFile
            );

            // Return generated file path
            return outputFile;
        }
    }

    private void replacePlaceholders(
            XWPFDocument document,
            Map<String, String> replacements) {

        // Process normal document paragraphs
        for (XWPFParagraph paragraph :
                document.getParagraphs()) {

            replaceInParagraph(
                    paragraph,
                    replacements
            );
        }
    }

    private void replaceInParagraph(
            XWPFParagraph paragraph,
            Map<String, String> replacements) {

        /*
         * Get the complete text of the paragraph.
         *
         * This handles placeholders that Word may
         * have internally split across multiple runs.
         */
        String originalText =
                paragraph.getText();

        if (originalText == null ||
                originalText.isEmpty()) {

            return;
        }

        String replacedText =
                originalText;

        // Replace all placeholders
        for (Map.Entry<String, String> entry :
                replacements.entrySet()) {

            replacedText =
                    replacedText.replace(
                            entry.getKey(),
                            entry.getValue()
                    );
        }

        // Nothing changed
        if (originalText.equals(replacedText)) {
            return;
        }

        // Remove existing runs
        for (int i = paragraph.getRuns().size() - 1;
             i >= 0;
             i--) {

            paragraph.removeRun(i);
        }

        // Create new run with replaced text
        XWPFRun newRun =
                paragraph.createRun();

        newRun.setText(
                replacedText
        );
    }
}