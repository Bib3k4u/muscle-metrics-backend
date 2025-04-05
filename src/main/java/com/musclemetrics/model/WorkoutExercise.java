package com.musclemetrics.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "workout_exercises")
public class WorkoutExercise {
    @Id
    private String id;

    @DBRef
    private ExerciseTemplate exerciseTemplate;

    private List<ExerciseSet> sets = new ArrayList<>();

    private Double totalVolume;

    // Constructors
    public WorkoutExercise() {
    }

    public WorkoutExercise(ExerciseTemplate exerciseTemplate) {
        this.exerciseTemplate = exerciseTemplate;
        this.totalVolume = 0.0;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ExerciseTemplate getExerciseTemplate() {
        return exerciseTemplate;
    }

    public void setExerciseTemplate(ExerciseTemplate exerciseTemplate) {
        this.exerciseTemplate = exerciseTemplate;
    }

    public List<ExerciseSet> getSets() {
        return sets;
    }

    public void setSets(List<ExerciseSet> sets) {
        this.sets = sets;
        calculateTotalVolume();
    }

    public Double getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(Double totalVolume) {
        this.totalVolume = totalVolume;
    }

    // Helper method to calculate total volume
    public void calculateTotalVolume() {
        double total = 0.0;
        for (ExerciseSet set : sets) {
            if (set.getWeight() != null) {
                total += set.getReps() * set.getWeight();
            }
        }
        this.totalVolume = total;
    }

    // Convenience method to add a set and recalculate volume
    public void addSet(ExerciseSet set) {
        this.sets.add(set);
        calculateTotalVolume();
    }

    // Inner class for exercise sets
    public static class ExerciseSet {
        private Integer reps;
        private Double weight;
        private Boolean completed;

        public ExerciseSet() {
            this.completed = false;
        }

        public ExerciseSet(Integer reps, Double weight) {
            this.reps = reps;
            this.weight = weight;
            this.completed = false;
        }

        public ExerciseSet(Integer reps, Double weight, Boolean completed) {
            this.reps = reps;
            this.weight = weight;
            this.completed = completed;
        }

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