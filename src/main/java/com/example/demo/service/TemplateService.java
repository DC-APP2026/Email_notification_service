package com.example.demo.service;

import com.example.demo.model.Trainee;

import org.springframework.stereotype.Service;

@Service
public class TemplateService {

    public String replacePlaceholders(
            String message,
            Trainee trainee) {

        message = replace(
                message,
                "{{traineeId}}",
                trainee.getTraineeId()
        );

        message = replace(
                message,
                "{{traineeName}}",
                trainee.getTraineeName()
        );

        message = replace(
                message,
                "{{course}}",
                trainee.getCourse()
        );

        message = replace(
                message,
                "{{trainerName}}",
                trainee.getTrainerName()
        );

        message = replace(
                message,
                "{{trainerId}}",
                trainee.getTrainerId()
        );

        message = replace(
                message,
                "{{startDate}}",
                trainee.getStartDate()
        );

        message = replace(
                message,
                "{{completionDate}}",
                trainee.getCompletionDate()
        );

        message = replace(
                message,
                "{{duration}}",
                trainee.getDuration()
        );

        message = replace(
                message,
                "{{adminName}}",
                trainee.getAdminName()
        );

        message = replace(
                message,
                "{{email}}",
                trainee.getEmail()
        );

        return message;
    }


    // Null-safe replacement
    private String replace(
            String message,
            String placeholder,
            String value) {

        if (value == null) {
            value = "";
        }

        return message.replace(
                placeholder,
                value
        );
    }
}