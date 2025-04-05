package com.musclemetrics.service;

import com.musclemetrics.dto.workout.WorkoutExerciseRequest;
import com.musclemetrics.dto.workout.WorkoutRequest;
import com.musclemetrics.model.Workout;
import com.musclemetrics.model.WorkoutExercise;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface WorkoutService {
    Workout createWorkout(WorkoutRequest request);

    Workout findById(String id);

    List<Workout> findAllForCurrentUser();

    List<Workout> findByDateRange(LocalDate startDate, LocalDate endDate);

    List<Workout> findByTargetMuscleGroup(String muscleGroupId);

    Workout update(String id, WorkoutRequest request);

    void delete(String id);

    WorkoutExercise addExerciseToWorkout(String workoutId, WorkoutExerciseRequest request);

    WorkoutExercise updateExerciseInWorkout(String workoutId, String exerciseId, WorkoutExerciseRequest request);

    void removeExerciseFromWorkout(String workoutId, String exerciseId);

    Workout copyWorkout(String workoutId, LocalDate newDate);

    Workout createWorkoutFromTemplate(String templateId, LocalDate date);

    List<Map<String, Object>> getExerciseHistory(String exerciseTemplateId, LocalDate startDate);
}