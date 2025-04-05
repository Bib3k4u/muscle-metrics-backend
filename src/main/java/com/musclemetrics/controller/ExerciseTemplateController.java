package com.musclemetrics.controller;

import com.musclemetrics.dto.exercise.ExerciseTemplateRequest;
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
    public ResponseEntity<List<ExerciseTemplate>> getAllExerciseTemplates() {
        List<ExerciseTemplate> exerciseTemplates = exerciseTemplateService.findAll();
        return ResponseEntity.ok(exerciseTemplates);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ExerciseTemplate> getExerciseTemplateById(@PathVariable String id) {
        ExerciseTemplate exerciseTemplate = exerciseTemplateService.findById(id);
        return ResponseEntity.ok(exerciseTemplate);
    }

    @GetMapping("/by-muscle-group/{muscleGroupId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ExerciseTemplate>> getExerciseTemplatesByMuscleGroup(
            @PathVariable String muscleGroupId) {
        MuscleGroup muscleGroup = muscleGroupService.findById(muscleGroupId);
        List<ExerciseTemplate> exerciseTemplates = exerciseTemplateService.findByMuscleGroup(muscleGroup);
        return ResponseEntity.ok(exerciseTemplates);
    }

    @GetMapping("/public/by-muscle-group/{muscleGroupId}")
    public ResponseEntity<List<ExerciseTemplate>> getPublicExerciseTemplatesByMuscleGroup(
            @PathVariable String muscleGroupId) {
        try {
            MuscleGroup muscleGroup = muscleGroupService.findById(muscleGroupId);
            List<ExerciseTemplate> exerciseTemplates = exerciseTemplateService.findByMuscleGroup(muscleGroup);
            return ResponseEntity.ok(exerciseTemplates);
        } catch (Exception e) {
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ExerciseTemplate> createExerciseTemplate(
            @Valid @RequestBody ExerciseTemplateRequest request) {
        ExerciseTemplate newExerciseTemplate = exerciseTemplateService.createExerciseTemplate(request);
        return ResponseEntity.ok(newExerciseTemplate);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ExerciseTemplate> updateExerciseTemplate(@PathVariable String id,
            @Valid @RequestBody ExerciseTemplateRequest request) {
        ExerciseTemplate updatedExerciseTemplate = exerciseTemplateService.update(id, request);
        return ResponseEntity.ok(updatedExerciseTemplate);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteExerciseTemplate(@PathVariable String id) {
        exerciseTemplateService.delete(id);
        return ResponseEntity.ok().build();
    }
}