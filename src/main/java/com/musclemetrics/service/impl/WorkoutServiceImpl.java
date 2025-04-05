package com.musclemetrics.service.impl;

import com.musclemetrics.dto.workout.WorkoutExerciseRequest;
import com.musclemetrics.dto.workout.WorkoutRequest;
import com.musclemetrics.model.*;
import com.musclemetrics.repository.WorkoutExerciseRepository;
import com.musclemetrics.repository.WorkoutRepository;
import com.musclemetrics.service.ExerciseTemplateService;
import com.musclemetrics.service.MuscleGroupService;
import com.musclemetrics.service.UserService;
import com.musclemetrics.service.WorkoutService;
import com.musclemetrics.service.WorkoutTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WorkoutServiceImpl implements WorkoutService {

    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private WorkoutExerciseRepository workoutExerciseRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private MuscleGroupService muscleGroupService;

    @Autowired
    private ExerciseTemplateService exerciseTemplateService;

    @Autowired
    private WorkoutTemplateService workoutTemplateService;

    @Override
    public Workout createWorkout(WorkoutRequest request) {
        User currentUser = userService.getCurrentUser();

        Workout workout = new Workout(currentUser, request.getDate(), request.getName());

        if (request.getTargetMuscleGroupIds() != null && !request.getTargetMuscleGroupIds().isEmpty()) {
            List<MuscleGroup> targetMuscleGroups = request.getTargetMuscleGroupIds().stream()
                    .map(muscleGroupService::findById)
                    .collect(Collectors.toList());
            workout.setTargetMuscleGroups(targetMuscleGroups);
        }

        if (request.getNotes() != null) {
            workout.setNotes(request.getNotes());
        }

        return workoutRepository.save(workout);
    }

    @Override
    public Workout findById(String id) {
        User currentUser = userService.getCurrentUser();
        Workout workout = workoutRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Workout with id " + id + " not found"));

        // Ensure the workout belongs to the current user
        if (!workout.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Access denied");
        }

        return workout;
    }

    @Override
    public List<Workout> findAllForCurrentUser() {
        User currentUser = userService.getCurrentUser();
        return workoutRepository.findByUserIdOrderByDateDesc(currentUser.getId());
    }

    @Override
    public List<Workout> findByDateRange(LocalDate startDate, LocalDate endDate) {
        User currentUser = userService.getCurrentUser();
        return workoutRepository.findByUserAndDateBetween(currentUser, startDate, endDate);
    }

    @Override
    public List<Workout> findByTargetMuscleGroup(String muscleGroupId) {
        User currentUser = userService.getCurrentUser();
        MuscleGroup muscleGroup = muscleGroupService.findById(muscleGroupId);
        return workoutRepository.findByUserAndTargetMuscleGroupsContains(currentUser, muscleGroup);
    }

    @Override
    public Workout update(String id, WorkoutRequest request) {
        Workout workout = findById(id);

        workout.setName(request.getName());
        workout.setDate(request.getDate());

        if (request.getTargetMuscleGroupIds() != null) {
            List<MuscleGroup> targetMuscleGroups = request.getTargetMuscleGroupIds().stream()
                    .map(muscleGroupService::findById)
                    .collect(Collectors.toList());
            workout.setTargetMuscleGroups(targetMuscleGroups);
        }

        if (request.getNotes() != null) {
            workout.setNotes(request.getNotes());
        }

        return workoutRepository.save(workout);
    }

    @Override
    public void delete(String id) {
        Workout workout = findById(id);
        workoutRepository.delete(workout);
    }

    @Override
    public WorkoutExercise addExerciseToWorkout(String workoutId, WorkoutExerciseRequest request) {
        Workout workout = findById(workoutId);

        ExerciseTemplate exerciseTemplate = exerciseTemplateService.findById(request.getExerciseTemplateId());
        WorkoutExercise workoutExercise = new WorkoutExercise(exerciseTemplate);

        // Add sets
        if (request.getSets() != null) {
            List<WorkoutExercise.ExerciseSet> exerciseSets = request.getSets().stream()
                    .map(setRequest -> new WorkoutExercise.ExerciseSet(setRequest.getReps(), setRequest.getWeight()))
                    .collect(Collectors.toList());
            workoutExercise.setSets(exerciseSets);
        }

        workoutExercise = workoutExerciseRepository.save(workoutExercise);
        workout.addExercise(workoutExercise);
        workoutRepository.save(workout);

        return workoutExercise;
    }

    @Override
    public WorkoutExercise updateExerciseInWorkout(String workoutId, String exerciseId,
            WorkoutExerciseRequest request) {
        Workout workout = findById(workoutId);

        // Find the exercise in the workout
        WorkoutExercise workoutExercise = workout.getExercises().stream()
                .filter(exercise -> exercise.getId().equals(exerciseId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Exercise with id " + exerciseId + " not found in workout"));

        // Update the exercise template if needed
        if (!workoutExercise.getExerciseTemplate().getId().equals(request.getExerciseTemplateId())) {
            ExerciseTemplate exerciseTemplate = exerciseTemplateService.findById(request.getExerciseTemplateId());
            workoutExercise.setExerciseTemplate(exerciseTemplate);
        }

        // Update the sets
        if (request.getSets() != null) {
            List<WorkoutExercise.ExerciseSet> exerciseSets = request.getSets().stream()
                    .map(setRequest -> {
                        WorkoutExercise.ExerciseSet set = new WorkoutExercise.ExerciseSet(
                                setRequest.getReps(),
                                setRequest.getWeight());
                        if (setRequest.getCompleted() != null) {
                            set.setCompleted(setRequest.getCompleted());
                        }
                        return set;
                    })
                    .collect(Collectors.toList());
            workoutExercise.setSets(exerciseSets);
        }

        // Save the updated exercise
        return workoutExerciseRepository.save(workoutExercise);
    }

    @Override
    public void removeExerciseFromWorkout(String workoutId, String exerciseId) {
        Workout workout = findById(workoutId);

        // Find the exercise in the workout
        WorkoutExercise workoutExercise = workout.getExercises().stream()
                .filter(exercise -> exercise.getId().equals(exerciseId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Exercise with id " + exerciseId + " not found in workout"));

        // Remove the exercise from the workout
        workout.getExercises().remove(workoutExercise);
        workoutRepository.save(workout);

        // Delete the exercise
        workoutExerciseRepository.delete(workoutExercise);
    }

    @Override
    public Workout copyWorkout(String workoutId, LocalDate newDate) {
        Workout sourceWorkout = findById(workoutId);

        // Create a new workout based on the source workout
        Workout newWorkout = new Workout(userService.getCurrentUser(), newDate, sourceWorkout.getName());
        newWorkout.setTargetMuscleGroups(new ArrayList<>(sourceWorkout.getTargetMuscleGroups()));
        newWorkout.setNotes(sourceWorkout.getNotes());

        // Save the new workout first to get an ID
        newWorkout = workoutRepository.save(newWorkout);

        // Copy all exercises from the source workout
        List<WorkoutExercise> newExercises = new ArrayList<>();
        for (WorkoutExercise sourceExercise : sourceWorkout.getExercises()) {
            WorkoutExercise newExercise = new WorkoutExercise(sourceExercise.getExerciseTemplate());

            // Copy all sets from the source exercise
            List<WorkoutExercise.ExerciseSet> newSets = sourceExercise.getSets().stream()
                    .map(sourceSet -> new WorkoutExercise.ExerciseSet(sourceSet.getReps(), sourceSet.getWeight()))
                    .collect(Collectors.toList());

            newExercise.setSets(newSets);
            newExercise = workoutExerciseRepository.save(newExercise);
            newExercises.add(newExercise);
        }

        newWorkout.setExercises(newExercises);
        return workoutRepository.save(newWorkout);
    }

    @Override
    public Workout createWorkoutFromTemplate(String templateId, LocalDate date) {
        User currentUser = userService.getCurrentUser();

        // Get the workout template
        WorkoutTemplate template = workoutTemplateService.getWorkoutTemplateById(templateId)
                .orElseThrow(() -> new RuntimeException("Workout template with id " + templateId + " not found"));

        // Create a new workout based on the template
        Workout workout = new Workout(currentUser, date, template.getName());

        // Set target muscle groups if available
        if (template.getTargetMuscleGroups() != null && !template.getTargetMuscleGroups().isEmpty()) {
            List<MuscleGroup> muscleGroups = template.getTargetMuscleGroups().stream()
                    .map(muscleGroupService::findById)
                    .collect(Collectors.toList());
            workout.setTargetMuscleGroups(muscleGroups);
        }

        // Set description as notes if available
        if (template.getDescription() != null && !template.getDescription().isEmpty()) {
            workout.setNotes(template.getDescription());
        }

        // Save the workout first to get an ID
        workout = workoutRepository.save(workout);

        // Copy exercises from template
        List<WorkoutExercise> workoutExercises = new ArrayList<>();
        for (WorkoutExercise templateExercise : template.getExercises()) {
            WorkoutExercise newExercise = new WorkoutExercise(templateExercise.getExerciseTemplate());

            // Copy sets but set completed to false
            if (templateExercise.getSets() != null && !templateExercise.getSets().isEmpty()) {
                List<WorkoutExercise.ExerciseSet> exerciseSets = templateExercise.getSets().stream()
                        .map(set -> {
                            WorkoutExercise.ExerciseSet newSet = new WorkoutExercise.ExerciseSet(
                                    set.getReps(), set.getWeight());
                            newSet.setCompleted(false);
                            return newSet;
                        })
                        .collect(Collectors.toList());
                newExercise.setSets(exerciseSets);
            }

            newExercise = workoutExerciseRepository.save(newExercise);
            workoutExercises.add(newExercise);
        }

        workout.setExercises(workoutExercises);
        return workoutRepository.save(workout);
    }

    @Override
    public List<Map<String, Object>> getExerciseHistory(String exerciseTemplateId, LocalDate startDate) {
        User currentUser = userService.getCurrentUser();
        ExerciseTemplate exerciseTemplate = exerciseTemplateService.findById(exerciseTemplateId);

        // If no start date provided, use 3 months ago as default
        if (startDate == null) {
            startDate = LocalDate.now().minusMonths(3);
        }

        // Get all workouts in date range
        List<Workout> workouts = workoutRepository.findByUserAndDateBetween(
                currentUser,
                startDate,
                LocalDate.now());

        List<Map<String, Object>> historyData = new ArrayList<>();

        // Go through all workouts and find exercises with the given template
        for (Workout workout : workouts) {
            for (WorkoutExercise exercise : workout.getExercises()) {
                if (exercise.getExerciseTemplate().getId().equals(exerciseTemplateId)) {
                    // Calculate total volume for this exercise
                    double totalVolume = 0;
                    for (WorkoutExercise.ExerciseSet set : exercise.getSets()) {
                        totalVolume += set.getReps() * set.getWeight();
                    }

                    // Create history entry
                    Map<String, Object> entry = new HashMap<>();
                    entry.put("date", workout.getDate());
                    entry.put("workoutId", workout.getId());
                    entry.put("workoutName", workout.getName());
                    entry.put("exerciseId", exercise.getId());
                    entry.put("sets", exercise.getSets().size());
                    entry.put("totalVolume", totalVolume);

                    historyData.add(entry);
                }
            }
        }

        // Sort by date (oldest first)
        historyData.sort((a, b) -> ((LocalDate) a.get("date")).compareTo((LocalDate) b.get("date")));

        return historyData;
    }
}