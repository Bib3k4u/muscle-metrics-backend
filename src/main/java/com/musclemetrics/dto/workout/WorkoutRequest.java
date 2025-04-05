package com.musclemetrics.dto.workout;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public class WorkoutRequest {

    @NotBlank
    private String name;

    @NotNull
    private LocalDate date;

    private List<String> targetMuscleGroupIds;

    private String notes;

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<String> getTargetMuscleGroupIds() {
        return targetMuscleGroupIds;
    }

    public void setTargetMuscleGroupIds(List<String> targetMuscleGroupIds) {
        this.targetMuscleGroupIds = targetMuscleGroupIds;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}