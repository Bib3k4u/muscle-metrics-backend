package com.musclemetrics.dto.workout;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class WorkoutExerciseRequest {

    @NotBlank
    private String exerciseTemplateId;

    @NotEmpty
    private List<ExerciseSetRequest> sets;

    // Getters and setters
    public String getExerciseTemplateId() {
        return exerciseTemplateId;
    }

    public void setExerciseTemplateId(String exerciseTemplateId) {
        this.exerciseTemplateId = exerciseTemplateId;
    }

    public List<ExerciseSetRequest> getSets() {
        return sets;
    }

    public void setSets(List<ExerciseSetRequest> sets) {
        this.sets = sets;
    }

    // Inner class for exercise sets
    public static class ExerciseSetRequest {
        private Integer reps;
        private Double weight;
        private Boolean completed;

        public Integer getReps() {
            return reps;
        }

        public void setReps(Integer reps) {
            this.reps = reps;
        }

        public Double getWeight() {
            return weight;
        }

        public void setWeight(Double weight) {
            this.weight = weight;
        }

        public Boolean getCompleted() {
            return completed;
        }

        public void setCompleted(Boolean completed) {
            this.completed = completed;
        }
    }
}