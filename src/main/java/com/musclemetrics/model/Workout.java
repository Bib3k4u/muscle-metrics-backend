package com.musclemetrics.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "workouts")
public class Workout {
    @Id
    private String id;

    @DBRef
    private User user;

    private LocalDate date;

    private DayOfWeek dayOfWeek;

    private String name;

    private List<MuscleGroup> targetMuscleGroups = new ArrayList<>();

    @DBRef
    private List<WorkoutExercise> exercises = new ArrayList<>();

    private String notes;

    // Constructors
    public Workout() {
    }

    public Workout(User user, LocalDate date, String name) {
        this.user = user;
        this.date = date;
        this.dayOfWeek = date.getDayOfWeek();
        this.name = name;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
        this.dayOfWeek = date.getDayOfWeek();
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MuscleGroup> getTargetMuscleGroups() {
        return targetMuscleGroups;
    }

    public void setTargetMuscleGroups(List<MuscleGroup> targetMuscleGroups) {
        this.targetMuscleGroups = targetMuscleGroups;
    }

    public List<WorkoutExercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<WorkoutExercise> exercises) {
        this.exercises = exercises;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // Helper method to add an exercise
    public void addExercise(WorkoutExercise exercise) {
        this.exercises.add(exercise);
    }

    // Helper method to add a target muscle group
    public void addTargetMuscleGroup(MuscleGroup muscleGroup) {
        this.targetMuscleGroups.add(muscleGroup);
    }
}