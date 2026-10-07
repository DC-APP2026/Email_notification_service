package com.example.demo.controller;

import com.example.demo.service.PdfService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;

@RestController
@RequestMapping("/api/pdf")
public class PdfController {

        private final PdfService pdfService;

        public PdfController(PdfService pdfService) {
        this.pdfService = pdfService;
        }

        @PostMapping("/convert")
        public ResponseEntity<?> convertToPdf(
                @RequestParam String fileName) {

        try {

                Path docxPath =
                        Path.of(
                                "generated-certificates",
                                fileName
                        );

                Path pdfPath =
                        pdfService.convertToPdf(
                                docxPath
                        );

                return ResponseEntity.ok(
                        "PDF generated successfully: "
                                + pdfPath
                );

        } catch (Exception e) {

                return ResponseEntity.internalServerError().body("PDF conversion failed: "+ e.getMessage()
                );
        }
        }
}