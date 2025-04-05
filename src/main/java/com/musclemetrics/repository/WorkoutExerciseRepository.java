package com.musclemetrics.repository;

import com.musclemetrics.model.ExerciseTemplate;
import com.musclemetrics.model.WorkoutExercise;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface WorkoutExerciseRepository extends MongoRepository<WorkoutExercise, String> {
    List<WorkoutExercise> findByExerciseTemplate(ExerciseTemplate exerciseTemplate);
}