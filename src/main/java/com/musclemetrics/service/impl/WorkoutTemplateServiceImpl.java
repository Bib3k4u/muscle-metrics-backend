package com.musclemetrics.service.impl;

import com.musclemetrics.exception.ResourceNotFoundException;
import com.musclemetrics.model.WorkoutTemplate;
import com.musclemetrics.repository.WorkoutTemplateRepository;
import com.musclemetrics.service.WorkoutTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WorkoutTemplateServiceImpl implements WorkoutTemplateService {

    private final WorkoutTemplateRepository workoutTemplateRepository;

    @Autowired
    public WorkoutTemplateServiceImpl(WorkoutTemplateRepository workoutTemplateRepository) {
        this.workoutTemplateRepository = workoutTemplateRepository;
    }

    @Override
    public List<WorkoutTemplate> getAllWorkoutTemplates() {
        return workoutTemplateRepository.findAll();
    }

    @Override
    public List<WorkoutTemplate> getFeaturedWorkoutTemplates() {
        return workoutTemplateRepository.findByFeatured(true);
    }

    @Override
    public List<WorkoutTemplate> getWorkoutTemplatesByUser(String userId) {
        return workoutTemplateRepository.findByCreatedBy(userId);
    }

    @Override
    public List<WorkoutTemplate> getWorkoutTemplatesByType(String type) {
        return workoutTemplateRepository.findByType(type);
    }

    @Override
    public Optional<WorkoutTemplate> getWorkoutTemplateById(String id) {
        return workoutTemplateRepository.findById(id);
    }

    @Override
    public WorkoutTemplate createWorkoutTemplate(WorkoutTemplate workoutTemplate) {
        return workoutTemplateRepository.save(workoutTemplate);
    }

    @Override
    public WorkoutTemplate updateWorkoutTemplate(String id, WorkoutTemplate workoutTemplateDetails) {
        WorkoutTemplate workoutTemplate = workoutTemplateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("WorkoutTemplate not found with id: " + id));

        workoutTemplate.setName(workoutTemplateDetails.getName());
        workoutTemplate.setType(workoutTemplateDetails.getType());
        workoutTemplate.setDescription(workoutTemplateDetails.getDescription());
        workoutTemplate.setTargetMuscleGroups(workoutTemplateDetails.getTargetMuscleGroups());
        workoutTemplate.setExercises(workoutTemplateDetails.getExercises());

        return workoutTemplateRepository.save(workoutTemplate);
    }

    @Override
    public void deleteWorkoutTemplate(String id) {
        WorkoutTemplate workoutTemplate = workoutTemplateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("WorkoutTemplate not found with id: " + id));

        workoutTemplateRepository.delete(workoutTemplate);
    }

    @Override
    public WorkoutTemplate setWorkoutTemplateFeatured(String id, boolean featured) {
        WorkoutTemplate workoutTemplate = workoutTemplateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("WorkoutTemplate not found with id: " + id));

        workoutTemplate.setFeatured(featured);
        return workoutTemplateRepository.save(workoutTemplate);
    }
}