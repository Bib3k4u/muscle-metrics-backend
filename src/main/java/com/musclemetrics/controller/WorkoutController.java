package com.musclemetrics.controller;

import com.musclemetrics.dto.workout.WorkoutExerciseRequest;
import com.musclemetrics.dto.workout.WorkoutRequest;
import com.musclemetrics.model.Workout;
import com.musclemetrics.model.WorkoutExercise;
import com.musclemetrics.service.WorkoutService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/workouts")
@CrossOrigin(origins = "*", maxAge = 3600)
public class WorkoutController {

    @Autowired
    private WorkoutService workoutService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Workout>> getAllWorkouts() {
        List<Workout> workouts = workoutService.findAllForCurrentUser();
        return ResponseEntity.ok(workouts);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Workout> getWorkoutById(@PathVariable String id) {
        Workout workout = workoutService.findById(id);
        return ResponseEntity.ok(workout);
    }

    @GetMapping("/by-date-range")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Workout>> getWorkoutsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Workout> workouts = workoutService.findByDateRange(startDate, endDate);
        return ResponseEntity.ok(workouts);
    }

    @GetMapping("/by-muscle-group/{muscleGroupId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Workout>> getWorkoutsByMuscleGroup(@PathVariable String muscleGroupId) {
        List<Workout> workouts = workoutService.findByTargetMuscleGroup(muscleGroupId);
        return ResponseEntity.ok(workouts);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Workout> createWorkout(@Valid @RequestBody WorkoutRequest request) {
        Workout newWorkout = workoutService.createWorkout(request);
        return ResponseEntity.ok(newWorkout);
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Workout> updateWorkout(@PathVariable String id, @Valid @RequestBody WorkoutRequest request) {
        Workout updatedWorkout = workoutService.update(id, request);
        return ResponseEntity.ok(updatedWorkout);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteWorkout(@PathVariable String id) {
        workoutService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{workoutId}/exercises")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<WorkoutExercise> addExerciseToWorkout(
            @PathVariable String workoutId,
            @Valid @RequestBody WorkoutExerciseRequest request) {
        WorkoutExercise newExercise = workoutService.addExerciseToWorkout(workoutId, request);
        return ResponseEntity.ok(newExercise);
    }

    @PutMapping("/{workoutId}/exercises/{exerciseId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<WorkoutExercise> updateExerciseInWorkout(
            @PathVariable String workoutId,
            @PathVariable String exerciseId,
            @Valid @RequestBody WorkoutExerciseRequest request) {
        WorkoutExercise updatedExercise = workoutService.updateExerciseInWorkout(workoutId, exerciseId, request);
        return ResponseEntity.ok(updatedExercise);
    }

    @DeleteMapping("/{workoutId}/exercises/{exerciseId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> removeExerciseFromWorkout(
            @PathVariable String workoutId,
            @PathVariable String exerciseId) {
        workoutService.removeExerciseFromWorkout(workoutId, exerciseId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{workoutId}/copy")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Workout> copyWorkout(
            @PathVariable String workoutId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate newDate) {
        Workout copiedWorkout = workoutService.copyWorkout(workoutId, newDate);
        return ResponseEntity.ok(copiedWorkout);
    }

    @PostMapping("/from-template/{templateId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Workout> createWorkoutFromTemplate(
            @PathVariable String templateId,
            @RequestBody Map<String, String> requestBody) {
        LocalDate date = LocalDate.parse(requestBody.get("date"));
        Workout newWorkout = workoutService.createWorkoutFromTemplate(templateId, date);
        return ResponseEntity.ok(newWorkout);
    }

    @GetMapping("/exercise-history/{exerciseTemplateId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Map<String, Object>>> getExerciseHistory(
            @PathVariable String exerciseTemplateId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
        List<Map<String, Object>> history = workoutService.getExerciseHistory(exerciseTemplateId, startDate);
        return ResponseEntity.ok(history);
    }
}