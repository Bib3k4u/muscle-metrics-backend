package com.musclemetrics.controller;

import com.musclemetrics.model.WorkoutTemplate;
import com.musclemetrics.service.WorkoutTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workout-templates")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class WorkoutTemplateController {

    private final WorkoutTemplateService workoutTemplateService;

    @Autowired
    public WorkoutTemplateController(WorkoutTemplateService workoutTemplateService) {
        this.workoutTemplateService = workoutTemplateService;
    }

    @GetMapping
    public ResponseEntity<List<WorkoutTemplate>> getAllWorkoutTemplates() {
        List<WorkoutTemplate> templates = workoutTemplateService.getAllWorkoutTemplates();
        return ResponseEntity.ok(templates);
    }

    @GetMapping("/featured")
    public ResponseEntity<List<WorkoutTemplate>> getFeaturedWorkoutTemplates() {
        List<WorkoutTemplate> templates = workoutTemplateService.getFeaturedWorkoutTemplates();
        return ResponseEntity.ok(templates);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<WorkoutTemplate>> getWorkoutTemplatesByType(@PathVariable String type) {
        List<WorkoutTemplate> templates = workoutTemplateService.getWorkoutTemplatesByType(type);
        return ResponseEntity.ok(templates);
    }

    @GetMapping("/user")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<WorkoutTemplate>> getUserWorkoutTemplates(
            @AuthenticationPrincipal UserDetails userDetails) {
        String userId = userDetails.getUsername();
        List<WorkoutTemplate> templates = workoutTemplateService.getWorkoutTemplatesByUser(userId);
        return ResponseEntity.ok(templates);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkoutTemplate> getWorkoutTemplateById(@PathVariable String id) {
        return workoutTemplateService.getWorkoutTemplateById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<WorkoutTemplate> createWorkoutTemplate(
            @RequestBody WorkoutTemplate workoutTemplate,
            @AuthenticationPrincipal UserDetails userDetails) {

        workoutTemplate.setCreatedBy(userDetails.getUsername());
        WorkoutTemplate createdTemplate = workoutTemplateService.createWorkoutTemplate(workoutTemplate);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTemplate);
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<WorkoutTemplate> updateWorkoutTemplate(
            @PathVariable String id,
            @RequestBody WorkoutTemplate workoutTemplate,
            @AuthenticationPrincipal UserDetails userDetails) {

        // Check if template belongs to user
        WorkoutTemplate existingTemplate = workoutTemplateService.getWorkoutTemplateById(id)
                .orElse(null);

        if (existingTemplate == null) {
            return ResponseEntity.notFound().build();
        }

        if (!existingTemplate.getCreatedBy().equals(userDetails.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        WorkoutTemplate updatedTemplate = workoutTemplateService.updateWorkoutTemplate(id, workoutTemplate);
        return ResponseEntity.ok(updatedTemplate);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteWorkoutTemplate(
            @PathVariable String id,
            @AuthenticationPrincipal UserDetails userDetails) {

        // Check if template belongs to user
        WorkoutTemplate existingTemplate = workoutTemplateService.getWorkoutTemplateById(id)
                .orElse(null);

        if (existingTemplate == null) {
            return ResponseEntity.notFound().build();
        }

        if (!existingTemplate.getCreatedBy().equals(userDetails.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        workoutTemplateService.deleteWorkoutTemplate(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/featured")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WorkoutTemplate> setWorkoutTemplateFeatured(
            @PathVariable String id,
            @RequestParam boolean featured) {

        WorkoutTemplate updatedTemplate = workoutTemplateService.setWorkoutTemplateFeatured(id, featured);
        return ResponseEntity.ok(updatedTemplate);
    }
}