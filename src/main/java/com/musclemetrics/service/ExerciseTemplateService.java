package com.musclemetrics.service;

import com.musclemetrics.dto.exercise.ExerciseTemplateRequest;
import com.musclemetrics.model.ExerciseTemplate;
import com.musclemetrics.model.MuscleGroup;

import java.util.List;

public interface ExerciseTemplateService {
    ExerciseTemplate createExerciseTemplate(ExerciseTemplateRequest request);

    ExerciseTemplate findById(String id);

    ExerciseTemplate findByName(String name);

    List<ExerciseTemplate> findAll();

    List<ExerciseTemplate> findByMuscleGroup(MuscleGroup muscleGroup);

    List<ExerciseTemplate> findByMuscleGroups(List<MuscleGroup> muscleGroups);

    ExerciseTemplate update(String id, ExerciseTemplateRequest request);

    void delete(String id);
}