package com.musclemetrics.controller;

import com.musclemetrics.model.MuscleGroup;
import com.musclemetrics.service.MuscleGroupService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/muscle-groups")
@CrossOrigin(origins = "*", maxAge = 3600)
public class MuscleGroupController {

    @Autowired
    private MuscleGroupService muscleGroupService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<MuscleGroup>> getAllMuscleGroups() {
        List<MuscleGroup> muscleGroups = muscleGroupService.findAll();
        return ResponseEntity.ok(muscleGroups);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MuscleGroup> getMuscleGroupById(@PathVariable String id) {
        MuscleGroup muscleGroup = muscleGroupService.findById(id);
        return ResponseEntity.ok(muscleGroup);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MuscleGroup> createMuscleGroup(@Valid @RequestBody MuscleGroupRequest request) {
        MuscleGroup newMuscleGroup = muscleGroupService.createMuscleGroup(request.getName());
        return ResponseEntity.ok(newMuscleGroup);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MuscleGroup> updateMuscleGroup(@PathVariable String id,
            @Valid @RequestBody MuscleGroupRequest request) {
        MuscleGroup updatedMuscleGroup = muscleGroupService.update(id, request.getName());
        return ResponseEntity.ok(updatedMuscleGroup);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteMuscleGroup(@PathVariable String id) {
        muscleGroupService.delete(id);
        return ResponseEntity.ok().build();
    }

    static class MuscleGroupRequest {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}