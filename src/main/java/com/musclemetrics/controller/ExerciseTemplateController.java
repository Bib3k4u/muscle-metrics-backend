package com.musclemetrics.controller;

import com.musclemetrics.dto.exercise.ExerciseTemplateRequest;
import com.musclemetrics.dto.exercise.ExerciseTemplateResponse;
import com.musclemetrics.model.ExerciseTemplate;
import com.musclemetrics.model.MuscleGroup;
import com.musclemetrics.service.ExerciseTemplateService;
import com.musclemetrics.service.MuscleGroupService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/exercise-templates")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ExerciseTemplateController {

    @Autowired
    private ExerciseTemplateService exerciseTemplateService;

    @Autowired
    private MuscleGroupService muscleGroupService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ExerciseTemplateResponse>> getAllExerciseTemplates() {
        try {
            List<ExerciseTemplate> exerciseTemplates = exerciseTemplateService.findAll();
            return ResponseEntity.ok(ExerciseTemplateResponse.fromTemplateList(exerciseTemplates));
        } catch (Exception e) {
            // Log the error for debugging
            System.err.println("Error retrieving exercise templates: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Public endpoint to retrieve all exercise templates without authentication
     * This helps prevent timeouts when the user is not authenticated
     */
    @GetMapping("/public")
    public ResponseEntity<List<ExerciseTemplateResponse>> getPublicExerciseTemplates() {
        try {
            List<ExerciseTemplate> exerciseTemplates = exerciseTemplateService.findAll();
            return ResponseEntity.ok(ExerciseTemplateResponse.fromTemplateList(exerciseTemplates));
        } catch (Exception e) {
            // Log the error
            System.err.println("Error retrieving public exercise templates: " + e.getMessage());
            e.printStackTrace();

            // Return an empty list instead of an error to avoid client-side issues
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ExerciseTemplateResponse> getExerciseTemplateById(@PathVariable String id) {
        try {
            ExerciseTemplate exerciseTemplate = exerciseTemplateService.findById(id);
            return ResponseEntity.ok(new ExerciseTemplateResponse(exerciseTemplate));
        } catch (Exception e) {
            // Log the error for debugging
            System.err.println("Error retrieving exercise template by ID: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Public endpoint to retrieve exercise template by ID without authentication
     */
    @GetMapping("/public/{id}")
    public ResponseEntity<ExerciseTemplateResponse> getPublicExerciseTemplateById(@PathVariable String id) {
        try {
            ExerciseTemplate exerciseTemplate = exerciseTemplateService.findById(id);
            return ResponseEntity.ok(new ExerciseTemplateResponse(exerciseTemplate));
        } catch (Exception e) {
            // Log the error
            System.err.println("Error retrieving public exercise template by ID: " + e.getMessage());
            e.printStackTrace();

            // Return a 404 Not Found instead of a 500 Internal Server Error
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/by-muscle-group/{muscleGroupId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ExerciseTemplateResponse>> getExerciseTemplatesByMuscleGroup(
            @PathVariable String muscleGroupId) {
        try {
            MuscleGroup muscleGroup = muscleGroupService.findById(muscleGroupId);
            List<ExerciseTemplate> exerciseTemplates = exerciseTemplateService.findByMuscleGroup(muscleGroup);
            return ResponseEntity.ok(ExerciseTemplateResponse.fromTemplateList(exerciseTemplates));
        } catch (Exception e) {
            // Log the error for debugging
            System.err.println("Error retrieving exercise templates by muscle group: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping("/public/by-muscle-group/{muscleGroupId}")
    public ResponseEntity<List<ExerciseTemplateResponse>> getPublicExerciseTemplatesByMuscleGroup(
            @PathVariable String muscleGroupId) {
        try {
            MuscleGroup muscleGroup = muscleGroupService.findById(muscleGroupId);
            List<ExerciseTemplate> exerciseTemplates = exerciseTemplateService.findByMuscleGroup(muscleGroup);
            return ResponseEntity.ok(ExerciseTemplateResponse.fromTemplateList(exerciseTemplates));
        } catch (Exception e) {
            // Log the error
            System.err.println("Error retrieving public exercise templates by muscle group: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ExerciseTemplateResponse> createExerciseTemplate(
            @Valid @RequestBody ExerciseTemplateRequest request) {
        ExerciseTemplate newExerciseTemplate = exerciseTemplateService.createExerciseTemplate(request);
        return ResponseEntity.ok(new ExerciseTemplateResponse(newExerciseTemplate));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ExerciseTemplateResponse> updateExerciseTemplate(@PathVariable String id,
            @Valid @RequestBody ExerciseTemplateRequest request) {
        ExerciseTemplate updatedExerciseTemplate = exerciseTemplateService.update(id, request);
        return ResponseEntity.ok(new ExerciseTemplateResponse(updatedExerciseTemplate));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteExerciseTemplate(@PathVariable String id) {
        exerciseTemplateService.delete(id);
        return ResponseEntity.ok().build();
    }
}