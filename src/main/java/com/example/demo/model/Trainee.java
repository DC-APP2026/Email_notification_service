package com.example.demo.model;

public class Trainee {

    private String traineeId;
    private String traineeName;
    private String course;
    private String trainerName;
    private String trainerId;
    private String startDate;
    private String completionDate;
    private String duration;
    private String adminName;
    private String email;

    public Trainee(
            String traineeId,
            String traineeName,
            String course,
            String trainerName,
            String trainerId,
            String startDate,
            String completionDate,
            String duration,
            String adminName,
            String email) {

        this.traineeId = traineeId;
        this.traineeName = traineeName;
        this.course = course;
        this.trainerName = trainerName;
        this.trainerId = trainerId;
        this.startDate = startDate;
        this.completionDate = completionDate;
        this.duration = duration;
        this.adminName = adminName;
        this.email = email;
    }

    public String getTraineeId() {
        return traineeId;
    }

    public String getTraineeName() {
        return traineeName;
    }

    public String getCourse() {
        return course;
    }

    public String getTrainerName() {
        return trainerName;
    }

    public String getTrainerId() {
        return trainerId;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getCompletionDate() {
        return completionDate;
    }

    public String getDuration() {
        return duration;
    }

    public String getAdminName() {
        return adminName;
    }

    public String getEmail() {
        return email;
    }
}