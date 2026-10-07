package com.example.demo.service;

import com.example.demo.model.Trainee;

import org.apache.poi.ss.usermodel.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelService {

    public List<Trainee> readTrainees() throws Exception {

        List<Trainee> trainees = new ArrayList<>();

        ClassPathResource resource =
                new ClassPathResource("input/Book.xlsx");

        try (InputStream inputStream = resource.getInputStream();
            Workbook workbook = WorkbookFactory.create(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);

            DataFormatter formatter = new DataFormatter();

            // Skip the header row
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);

                if (row == null) {
                    continue;
                }

                String traineeId =
                        formatter.formatCellValue(row.getCell(0));

                String traineeName =
                        formatter.formatCellValue(row.getCell(1));

                String course =
                        formatter.formatCellValue(row.getCell(2));

                String trainerName =
                        formatter.formatCellValue(row.getCell(3));

                String trainerId =
                        formatter.formatCellValue(row.getCell(4));

                String startDate =
                        formatter.formatCellValue(row.getCell(5));

                String completionDate =
                        formatter.formatCellValue(row.getCell(6));

                String duration =
                        formatter.formatCellValue(row.getCell(7));

                String adminName =
                        formatter.formatCellValue(row.getCell(8));

                String email =
                        formatter.formatCellValue(row.getCell(9));

                Trainee trainee = new Trainee(
                        traineeId,
                        traineeName,
                        course,
                        trainerName,
                        trainerId,
                        startDate,
                        completionDate,
                        duration,
                        adminName,
                        email
                );

                trainees.add(trainee);
            }
        }

        return trainees;
    }
    public Trainee findTraineeById(String traineeId) throws Exception {

    List<Trainee> trainees = readTrainees();

    for (Trainee trainee : trainees) {

        if (trainee.getTraineeId().equalsIgnoreCase(traineeId)) {
            return trainee;
        }
    }

    return null;
}
}