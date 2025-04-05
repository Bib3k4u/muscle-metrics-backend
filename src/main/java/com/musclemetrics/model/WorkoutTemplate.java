package com.musclemetrics.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Document(collection = "workout_templates")
public class WorkoutTemplate {
    @Id
    private String id;
    private String name;
    private String type;
    private String description;
    private List<String> targetMuscleGroups = new ArrayList<>();
    private List<WorkoutExercise> exercises = new ArrayList<>();
    private boolean featured = false;
    private String createdBy;

    public WorkoutTemplate() {
    }

    public WorkoutTemplate(String name, String type, String description, List<String> targetMuscleGroups,
            List<WorkoutExercise> exercises, boolean featured, String createdBy) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.targetMuscleGroups = targetMuscleGroups;
        this.exercises = exercises;
        this.featured = featured;
        this.createdBy = createdBy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getTargetMuscleGroups() {
        return targetMuscleGroups;
    }

    public void setTargetMuscleGroups(List<String> targetMuscleGroups) {
        this.targetMuscleGroups = targetMuscleGroups;
    }

    public void addMuscleGroup(String muscleGroupId) {
        this.targetMuscleGroups.add(muscleGroupId);
    }

    public List<WorkoutExercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<WorkoutExercise> exercises) {
        this.exercises = exercises;
    }

    public void addExercise(WorkoutExercise exercise) {
        this.exercises.add(exercise);
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}