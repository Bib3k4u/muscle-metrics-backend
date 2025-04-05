package com.musclemetrics.service;

import com.musclemetrics.model.WorkoutTemplate;

import java.util.List;
import java.util.Optional;

public interface WorkoutTemplateService {
    List<WorkoutTemplate> getAllWorkoutTemplates();

    List<WorkoutTemplate> getFeaturedWorkoutTemplates();

    List<WorkoutTemplate> getWorkoutTemplatesByUser(String userId);

    List<WorkoutTemplate> getWorkoutTemplatesByType(String type);

    Optional<WorkoutTemplate> getWorkoutTemplateById(String id);

    WorkoutTemplate createWorkoutTemplate(WorkoutTemplate workoutTemplate);

    WorkoutTemplate updateWorkoutTemplate(String id, WorkoutTemplate workoutTemplate);

    void deleteWorkoutTemplate(String id);

    WorkoutTemplate setWorkoutTemplateFeatured(String id, boolean featured);
}