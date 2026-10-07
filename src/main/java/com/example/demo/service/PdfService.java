package com.example.demo.service;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;

import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

@Service
public class PdfService {

    public Path convertToPdf(Path docxPath) throws Exception {

        // Check DOCX exists
        if (!Files.exists(docxPath)) {
            throw new IllegalArgumentException(
                "DOCX file not found: " + docxPath
            );
        }

        // Create PDF filename
        String fileName =
                docxPath.getFileName()
                        .toString()
                        .replaceFirst(
                                "(?i)\\.docx$",
                                ".pdf"
                        );

        Path pdfPath =
                docxPath.resolveSibling(fileName);

        System.out.println(
                "Converting DOCX: " + docxPath
        );

        System.out.println(
                "Output PDF: " + pdfPath
        );

        IConverter converter =
                LocalConverter.builder()
                        .baseFolder(
                                Files.createTempDirectory(
                                        "documents4j"
                                ).toFile()
                        )
                        .processTimeout(
                                120,
                                TimeUnit.SECONDS
                        )
                        .build();

        try {

            converter
                    .convert(
                            docxPath.toFile()
                    )
                    .as(DocumentType.DOCX)
                    .to(pdfPath.toFile())
                    .as(DocumentType.PDF)
                    .execute();

            System.out.println(
                    "PDF conversion completed."
            );

            return pdfPath;

        } finally {

            converter.shutDown();
        }
    }
}