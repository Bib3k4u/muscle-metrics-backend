package com.musclemetrics.config;

import com.musclemetrics.model.ExerciseTemplate;
import com.musclemetrics.model.MuscleGroup;
import com.musclemetrics.model.WorkoutExercise;
import com.musclemetrics.model.WorkoutTemplate;
import com.musclemetrics.repository.ExerciseTemplateRepository;
import com.musclemetrics.repository.MuscleGroupRepository;
import com.musclemetrics.repository.WorkoutTemplateRepository;
import com.musclemetrics.service.MuscleGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TemplatesDataInitializer implements CommandLineRunner {

    @Autowired
    private WorkoutTemplateRepository workoutTemplateRepository;

    @Autowired
    private ExerciseTemplateRepository exerciseTemplateRepository;

    @Autowired
    private MuscleGroupRepository muscleGroupRepository;

    @Override
    public void run(String... args) throws Exception {
        // Only seed data if templates don't exist
        if (workoutTemplateRepository.count() == 0) {
            createDefaultTemplates();
        }
    }

    private void createDefaultTemplates() {
        // Create or get muscle groups
        Map<String, MuscleGroup> muscleGroups = createMuscleGroups();

        // Create or get exercise templates
        Map<String, ExerciseTemplate> exerciseTemplates = createExerciseTemplates(muscleGroups);

        // Create workout templates
        createPushWorkoutTemplate(muscleGroups, exerciseTemplates);
        createPullWorkoutTemplate(muscleGroups, exerciseTemplates);
        createLegsWorkoutTemplate(muscleGroups, exerciseTemplates);
        createPush2WorkoutTemplate(muscleGroups, exerciseTemplates);
        createPull2WorkoutTemplate(muscleGroups, exerciseTemplates);
        createLegs2WorkoutTemplate(muscleGroups, exerciseTemplates);
    }

    private Map<String, MuscleGroup> createMuscleGroups() {
        Map<String, MuscleGroup> muscleGroups = new HashMap<>();

        // Define muscle groups
        String[] groupNames = {
                "Chest", "Back", "Shoulders", "Biceps", "Triceps",
                "Quadriceps", "Hamstrings", "Calves", "Glutes", "Traps", "Rear Delts"
        };

        for (String name : groupNames) {
            MuscleGroup group = muscleGroupRepository.findByNameIgnoreCase(name).orElse(null);
            if (group == null) {
                group = new MuscleGroup();
                group.setName(name);
                muscleGroupRepository.save(group);
            }
            muscleGroups.put(name.toLowerCase(), group);
        }

        return muscleGroups;
    }

    private Map<String, ExerciseTemplate> createExerciseTemplates(Map<String, MuscleGroup> muscleGroups) {
        Map<String, ExerciseTemplate> templates = new HashMap<>();

        // Define exercises with primary and secondary muscle groups
        Object[][] exercises = {
                { "Bench Press", muscleGroups.get("chest"), muscleGroups.get("triceps"), true },
                { "Incline Dumbbell Press", muscleGroups.get("chest"), muscleGroups.get("shoulders"), true },
                { "Machine Chest Fly", muscleGroups.get("chest"), null, true },
                { "Overhead Press", muscleGroups.get("shoulders"), muscleGroups.get("triceps"), true },
                { "Lateral Raise", muscleGroups.get("shoulders"), null, true },
                { "Tricep Pushdown", muscleGroups.get("triceps"), null, true },
                { "Overhead Tricep Extension", muscleGroups.get("triceps"), null, true },
                { "Deadlift", muscleGroups.get("back"), muscleGroups.get("hamstrings"), true },
                { "Pull-Up", muscleGroups.get("back"), muscleGroups.get("biceps"), false },
                { "Barbell Row", muscleGroups.get("back"), muscleGroups.get("biceps"), true },
                { "Lat Pulldown", muscleGroups.get("back"), null, true },
                { "Seated Cable Row", muscleGroups.get("back"), null, true },
                { "Barbell Curl", muscleGroups.get("biceps"), null, true },
                { "Hammer Curl", muscleGroups.get("biceps"), null, true },
                { "Squat", muscleGroups.get("quadriceps"), muscleGroups.get("glutes"), true },
                { "Leg Press", muscleGroups.get("quadriceps"), muscleGroups.get("glutes"), true },
                { "Leg Extension", muscleGroups.get("quadriceps"), null, true },
                { "Romanian Deadlift", muscleGroups.get("hamstrings"), muscleGroups.get("glutes"), true },
                { "Leg Curl", muscleGroups.get("hamstrings"), null, true },
                { "Standing Calf Raise", muscleGroups.get("calves"), null, true },
                { "Seated Calf Raise", muscleGroups.get("calves"), null, true },
                { "Incline Bench Press", muscleGroups.get("chest"), muscleGroups.get("shoulders"), true },
                { "Dumbbell Press", muscleGroups.get("chest"), muscleGroups.get("triceps"), true },
                { "Cable Crossover", muscleGroups.get("chest"), null, true },
                { "Dumbbell Shoulder Press", muscleGroups.get("shoulders"), muscleGroups.get("triceps"), true },
                { "Front Raise", muscleGroups.get("shoulders"), null, true },
                { "Skull Crusher", muscleGroups.get("triceps"), null, true },
                { "Tricep Dip", muscleGroups.get("triceps"), muscleGroups.get("chest"), false },
                { "Weighted Pull-Up", muscleGroups.get("back"), muscleGroups.get("biceps"), true },
                { "T-Bar Row", muscleGroups.get("back"), null, true },
                { "Chest Supported Row", muscleGroups.get("back"), null, true },
                { "Single Arm Row", muscleGroups.get("back"), null, true },
                { "Face Pull", muscleGroups.get("rear delts"), muscleGroups.get("traps"), true },
                { "Preacher Curl", muscleGroups.get("biceps"), null, true },
                { "Concentration Curl", muscleGroups.get("biceps"), null, true },
                { "Front Squat", muscleGroups.get("quadriceps"), muscleGroups.get("glutes"), true },
                { "Hack Squat", muscleGroups.get("quadriceps"), null, true },
                { "Bulgarian Split Squat", muscleGroups.get("quadriceps"), muscleGroups.get("glutes"), true },
                { "Good Morning", muscleGroups.get("hamstrings"), muscleGroups.get("back"), true },
                { "Glute Ham Raise", muscleGroups.get("hamstrings"), muscleGroups.get("glutes"), false },
                { "Leg Press Calf Raise", muscleGroups.get("calves"), null, true },
                { "Single Leg Calf Raise", muscleGroups.get("calves"), null, false }
        };

        for (Object[] exercise : exercises) {
            String name = (String) exercise[0];
            ExerciseTemplate template = exerciseTemplateRepository.findByNameIgnoreCase(name).orElse(null);

            if (template == null) {
                template = new ExerciseTemplate();
                template.setName(name);
                template.setPrimaryMuscleGroup((MuscleGroup) exercise[1]);
                template.setSecondaryMuscleGroup((MuscleGroup) exercise[2]);
                template.setRequiresWeight((Boolean) exercise[3]);
                exerciseTemplateRepository.save(template);
            }

            templates.put(name.toLowerCase().replace(" ", "-"), template);
        }

        return templates;
    }

    private void createPushWorkoutTemplate(Map<String, MuscleGroup> muscleGroups,
            Map<String, ExerciseTemplate> exercises) {
        WorkoutTemplate template = new WorkoutTemplate();
        template.setId("template-1");
        template.setName("Day 1: Push (Chest, Shoulders, Triceps)");
        template.setDescription("Focus on chest, front delts and triceps. Perfect for Monday.");
        template.setType("Push");
        template.setFeatured(true);
        template.setCreatedBy("system");

        // Set target muscle groups
        List<String> muscleGroupIds = new ArrayList<>();
        muscleGroupIds.add(muscleGroups.get("chest").getId());
        muscleGroupIds.add(muscleGroups.get("shoulders").getId());
        muscleGroupIds.add(muscleGroups.get("triceps").getId());
        template.setTargetMuscleGroups(muscleGroupIds);

        // Create exercises
        List<WorkoutExercise> templateExercises = new ArrayList<>();

        // Bench Press
        WorkoutExercise benchPress = createExerciseWithSets(
                exercises.get("bench-press"),
                new int[] { 12, 10, 8, 6 },
                new double[] { 60, 70, 80, 85 });
        templateExercises.add(benchPress);

        // Incline Dumbbell Press
        WorkoutExercise inclinePress = createExerciseWithSets(
                exercises.get("incline-dumbbell-press"),
                new int[] { 12, 10, 8 },
                new double[] { 20, 22.5, 25 });
        templateExercises.add(inclinePress);

        // Machine Chest Fly
        WorkoutExercise chestFly = createExerciseWithSets(
                exercises.get("machine-chest-fly"),
                new int[] { 15, 12, 10 },
                new double[] { 40, 45, 50 });
        templateExercises.add(chestFly);

        // Overhead Press
        WorkoutExercise overheadPress = createExerciseWithSets(
                exercises.get("overhead-press"),
                new int[] { 10, 8, 8 },
                new double[] { 40, 45, 45 });
        templateExercises.add(overheadPress);

        // Lateral Raise
        WorkoutExercise lateralRaise = createExerciseWithSets(
                exercises.get("lateral-raise"),
                new int[] { 15, 15, 12 },
                new double[] { 8, 8, 10 });
        templateExercises.add(lateralRaise);

        // Tricep Pushdown
        WorkoutExercise tricepPushdown = createExerciseWithSets(
                exercises.get("tricep-pushdown"),
                new int[] { 15, 12, 10 },
                new double[] { 25, 30, 35 });
        templateExercises.add(tricepPushdown);

        // Overhead Tricep Extension
        WorkoutExercise tricepExtension = createExerciseWithSets(
                exercises.get("overhead-tricep-extension"),
                new int[] { 12, 10, 10 },
                new double[] { 20, 25, 25 });
        templateExercises.add(tricepExtension);

        template.setExercises(templateExercises);
        workoutTemplateRepository.save(template);
    }

    private void createPullWorkoutTemplate(Map<String, MuscleGroup> muscleGroups,
            Map<String, ExerciseTemplate> exercises) {
        WorkoutTemplate template = new WorkoutTemplate();
        template.setId("template-2");
        template.setName("Day 2: Pull (Back, Biceps)");
        template.setDescription("Focus on back and biceps. Ideal for Tuesday.");
        template.setType("Pull");
        template.setFeatured(true);
        template.setCreatedBy("system");

        // Set target muscle groups
        List<String> muscleGroupIds = new ArrayList<>();
        muscleGroupIds.add(muscleGroups.get("back").getId());
        muscleGroupIds.add(muscleGroups.get("biceps").getId());
        muscleGroupIds.add(muscleGroups.get("traps").getId());
        template.setTargetMuscleGroups(muscleGroupIds);

        // Create exercises
        List<WorkoutExercise> templateExercises = new ArrayList<>();

        // Deadlift
        WorkoutExercise deadlift = createExerciseWithSets(
                exercises.get("deadlift"),
                new int[] { 8, 6, 4 },
                new double[] { 100, 120, 140 });
        templateExercises.add(deadlift);

        // Pull-Up
        WorkoutExercise pullUp = createExerciseWithSets(
                exercises.get("pull-up"),
                new int[] { 10, 8, 6 },
                new double[] { 0, 0, 0 });
        templateExercises.add(pullUp);

        // Add more exercises...
        // Barbell Row
        WorkoutExercise barbellRow = createExerciseWithSets(
                exercises.get("barbell-row"),
                new int[] { 12, 10, 8 },
                new double[] { 60, 70, 80 });
        templateExercises.add(barbellRow);

        // Lat Pulldown
        WorkoutExercise latPulldown = createExerciseWithSets(
                exercises.get("lat-pulldown"),
                new int[] { 12, 10, 10 },
                new double[] { 50, 60, 60 });
        templateExercises.add(latPulldown);

        // Seated Cable Row
        WorkoutExercise cableRow = createExerciseWithSets(
                exercises.get("seated-cable-row"),
                new int[] { 12, 10, 8 },
                new double[] { 55, 65, 75 });
        templateExercises.add(cableRow);

        // Barbell Curl
        WorkoutExercise barbellCurl = createExerciseWithSets(
                exercises.get("barbell-curl"),
                new int[] { 12, 10, 8 },
                new double[] { 30, 35, 40 });
        templateExercises.add(barbellCurl);

        // Hammer Curl
        WorkoutExercise hammerCurl = createExerciseWithSets(
                exercises.get("hammer-curl"),
                new int[] { 12, 10, 10 },
                new double[] { 12, 14, 14 });
        templateExercises.add(hammerCurl);

        template.setExercises(templateExercises);
        workoutTemplateRepository.save(template);
    }

    private void createLegsWorkoutTemplate(Map<String, MuscleGroup> muscleGroups,
            Map<String, ExerciseTemplate> exercises) {
        WorkoutTemplate template = new WorkoutTemplate();
        template.setId("template-3");
        template.setName("Day 3: Legs (Quads, Hamstrings, Calves)");
        template.setDescription("Focus on leg development. Recommended for Wednesday.");
        template.setType("Legs");
        template.setFeatured(true);
        template.setCreatedBy("system");

        // Set target muscle groups
        List<String> muscleGroupIds = new ArrayList<>();
        muscleGroupIds.add(muscleGroups.get("quadriceps").getId());
        muscleGroupIds.add(muscleGroups.get("hamstrings").getId());
        muscleGroupIds.add(muscleGroups.get("calves").getId());
        muscleGroupIds.add(muscleGroups.get("glutes").getId());
        template.setTargetMuscleGroups(muscleGroupIds);

        // Create exercises
        List<WorkoutExercise> templateExercises = new ArrayList<>();

        // Squat
        WorkoutExercise squat = createExerciseWithSets(
                exercises.get("squat"),
                new int[] { 12, 10, 8, 6 },
                new double[] { 80, 100, 120, 130 });
        templateExercises.add(squat);

        // Leg Press
        WorkoutExercise legPress = createExerciseWithSets(
                exercises.get("leg-press"),
                new int[] { 12, 10, 8 },
                new double[] { 120, 140, 160 });
        templateExercises.add(legPress);

        // Leg Extension
        WorkoutExercise legExtension = createExerciseWithSets(
                exercises.get("leg-extension"),
                new int[] { 15, 12, 12 },
                new double[] { 40, 45, 45 });
        templateExercises.add(legExtension);

        // Romanian Deadlift
        WorkoutExercise romanianDeadlift = createExerciseWithSets(
                exercises.get("romanian-deadlift"),
                new int[] { 12, 10, 10 },
                new double[] { 80, 90, 90 });
        templateExercises.add(romanianDeadlift);

        // Leg Curl
        WorkoutExercise legCurl = createExerciseWithSets(
                exercises.get("leg-curl"),
                new int[] { 15, 12, 12 },
                new double[] { 35, 40, 40 });
        templateExercises.add(legCurl);

        // Standing Calf Raise
        WorkoutExercise standingCalfRaise = createExerciseWithSets(
                exercises.get("standing-calf-raise"),
                new int[] { 15, 15, 15 },
                new double[] { 80, 80, 80 });
        templateExercises.add(standingCalfRaise);

        // Seated Calf Raise
        WorkoutExercise seatedCalfRaise = createExerciseWithSets(
                exercises.get("seated-calf-raise"),
                new int[] { 15, 15, 15 },
                new double[] { 40, 40, 40 });
        templateExercises.add(seatedCalfRaise);

        template.setExercises(templateExercises);
        workoutTemplateRepository.save(template);
    }

    private void createPush2WorkoutTemplate(Map<String, MuscleGroup> muscleGroups,
            Map<String, ExerciseTemplate> exercises) {
        WorkoutTemplate template = new WorkoutTemplate();
        template.setId("template-4");
        template.setName("Day 4: Push (Chest, Shoulders, Triceps)");
        template.setDescription("Second push day with different exercise variations. For Thursday.");
        template.setType("Push");
        template.setFeatured(true);
        template.setCreatedBy("system");

        // Set target muscle groups
        List<String> muscleGroupIds = new ArrayList<>();
        muscleGroupIds.add(muscleGroups.get("chest").getId());
        muscleGroupIds.add(muscleGroups.get("shoulders").getId());
        muscleGroupIds.add(muscleGroups.get("triceps").getId());
        template.setTargetMuscleGroups(muscleGroupIds);

        // Add exercises similar to Push day 1 but with variations
        List<WorkoutExercise> templateExercises = new ArrayList<>();

        // Create and add all exercises for Push day 2
        templateExercises.add(createExerciseWithSets(
                exercises.get("incline-bench-press"),
                new int[] { 12, 10, 8, 6 },
                new double[] { 50, 60, 70, 75 }));

        templateExercises.add(createExerciseWithSets(
                exercises.get("dumbbell-press"),
                new int[] { 12, 10, 8 },
                new double[] { 20, 22.5, 25 }));

        templateExercises.add(createExerciseWithSets(
                exercises.get("cable-crossover"),
                new int[] { 15, 12, 12 },
                new double[] { 15, 17.5, 17.5 }));

        templateExercises.add(createExerciseWithSets(
                exercises.get("dumbbell-shoulder-press"),
                new int[] { 12, 10, 8 },
                new double[] { 15, 17.5, 20 }));

        templateExercises.add(createExerciseWithSets(
                exercises.get("front-raise"),
                new int[] { 15, 12, 12 },
                new double[] { 8, 10, 10 }));

        templateExercises.add(createExerciseWithSets(
                exercises.get("skull-crusher"),
                new int[] { 12, 10, 10 },
                new double[] { 25, 30, 30 }));

        templateExercises.add(createExerciseWithSets(
                exercises.get("tricep-dip"),
                new int[] { 12, 10, 8 },
                new double[] { 0, 0, 0 }));

        template.setExercises(templateExercises);
        workoutTemplateRepository.save(template);
    }

    private void createPull2WorkoutTemplate(Map<String, MuscleGroup> muscleGroups,
            Map<String, ExerciseTemplate> exercises) {
        WorkoutTemplate template = new WorkoutTemplate();
        template.setId("template-5");
        template.setName("Day 5: Pull (Back, Biceps)");
        template.setDescription("Second pull day with exercise variations. Perfect for Friday.");
        template.setType("Pull");
        template.setFeatured(true);
        template.setCreatedBy("system");

        // Set target muscle groups
        List<String> muscleGroupIds = new ArrayList<>();
        muscleGroupIds.add(muscleGroups.get("back").getId());
        muscleGroupIds.add(muscleGroups.get("biceps").getId());
        muscleGroupIds.add(muscleGroups.get("rear delts").getId());
        template.setTargetMuscleGroups(muscleGroupIds);

        // Create exercises
        List<WorkoutExercise> templateExercises = new ArrayList<>();

        // Add exercises for Pull day 2
        templateExercises.add(createExerciseWithSets(
                exercises.get("weighted-pull-up"),
                new int[] { 8, 6, 6 },
                new double[] { 5, 10, 10 }));

        templateExercises.add(createExerciseWithSets(
                exercises.get("t-bar-row"),
                new int[] { 12, 10, 8 },
                new double[] { 40, 45, 50 }));

        templateExercises.add(createExerciseWithSets(
                exercises.get("chest-supported-row"),
                new int[] { 12, 10, 8 },
                new double[] { 20, 22.5, 25 }));

        templateExercises.add(createExerciseWithSets(
                exercises.get("single-arm-row"),
                new int[] { 12, 10, 10 },
                new double[] { 20, 22.5, 22.5 }));

        templateExercises.add(createExerciseWithSets(
                exercises.get("face-pull"),
                new int[] { 15, 15, 15 },
                new double[] { 25, 25, 25 }));

        templateExercises.add(createExerciseWithSets(
                exercises.get("preacher-curl"),
                new int[] { 12, 10, 8 },
                new double[] { 25, 30, 35 }));

        templateExercises.add(createExerciseWithSets(
                exercises.get("concentration-curl"),
                new int[] { 12, 10, 10 },
                new double[] { 10, 12, 12 }));

        template.setExercises(templateExercises);
        workoutTemplateRepository.save(template);
    }

    private void createLegs2WorkoutTemplate(Map<String, MuscleGroup> muscleGroups,
            Map<String, ExerciseTemplate> exercises) {
        WorkoutTemplate template = new WorkoutTemplate();
        template.setId("template-6");
        template.setName("Day 6: Legs (Quads, Hamstrings, Calves)");
        template.setDescription("Second legs day with different exercise variations. Ideal for Saturday.");
        template.setType("Legs");
        template.setFeatured(true);
        template.setCreatedBy("system");

        // Set target muscle groups
        List<String> muscleGroupIds = new ArrayList<>();
        muscleGroupIds.add(muscleGroups.get("quadriceps").getId());
        muscleGroupIds.add(muscleGroups.get("hamstrings").getId());
        muscleGroupIds.add(muscleGroups.get("calves").getId());
        muscleGroupIds.add(muscleGroups.get("glutes").getId());
        template.setTargetMuscleGroups(muscleGroupIds);

        // Create exercises
        List<WorkoutExercise> templateExercises = new ArrayList<>();

        // Add exercises for Legs day 2
        templateExercises.add(createExerciseWithSets(
                exercises.get("front-squat"),
                new int[] { 10, 8, 6 },
                new double[] { 60, 70, 80 }));

        templateExercises.add(createExerciseWithSets(
                exercises.get("hack-squat"),
                new int[] { 12, 10, 8 },
                new double[] { 80, 100, 120 }));

        templateExercises.add(createExerciseWithSets(
                exercises.get("bulgarian-split-squat"),
                new int[] { 10, 10, 8 },
                new double[] { 20, 20, 25 }));

        templateExercises.add(createExerciseWithSets(
                exercises.get("good-morning"),
                new int[] { 12, 10, 10 },
                new double[] { 40, 50, 50 }));

        templateExercises.add(createExerciseWithSets(
                exercises.get("glute-ham-raise"),
                new int[] { 12, 10, 10 },
                new double[] { 0, 0, 0 }));

        templateExercises.add(createExerciseWithSets(
                exercises.get("leg-press-calf-raise"),
                new int[] { 20, 15, 15 },
                new double[] { 100, 120, 120 }));

        templateExercises.add(createExerciseWithSets(
                exercises.get("single-leg-calf-raise"),
                new int[] { 15, 15, 15 },
                new double[] { 0, 0, 0 }));

        template.setExercises(templateExercises);
        workoutTemplateRepository.save(template);
    }

    private WorkoutExercise createExerciseWithSets(ExerciseTemplate template, int[] reps, double[] weights) {
        WorkoutExercise exercise = new WorkoutExercise(template);
        List<WorkoutExercise.ExerciseSet> sets = new ArrayList<>();

        for (int i = 0; i < reps.length; i++) {
            WorkoutExercise.ExerciseSet set = new WorkoutExercise.ExerciseSet(reps[i], weights[i]);
            set.setCompleted(false);
            sets.add(set);
        }

        exercise.setSets(sets);
        return exercise;
    }
}