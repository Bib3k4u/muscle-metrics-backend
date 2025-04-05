package com.musclemetrics.config;

import com.musclemetrics.model.ExerciseTemplate;
import com.musclemetrics.model.MuscleGroup;
import com.musclemetrics.model.User;
import com.musclemetrics.repository.ExerciseTemplateRepository;
import com.musclemetrics.repository.MuscleGroupRepository;
import com.musclemetrics.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private MuscleGroupRepository muscleGroupRepository;

    @Autowired
    private ExerciseTemplateRepository exerciseTemplateRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Map<String, MuscleGroup> muscleGroupMap = new HashMap<>();

    @Override
    public void run(String... args) {
        // Create admin user if it doesn't exist
        if (!userRepository.existsByEmail("admin@musclemetrics.com")) {
            User adminUser = new User("admin", "admin@musclemetrics.com", passwordEncoder.encode("admin1234"));
            Set<String> roles = new HashSet<>();
            roles.add("USER");
            roles.add("ADMIN");
            adminUser.setRoles(roles);
            userRepository.save(adminUser);
        }

        // Create muscle groups if they don't exist and store them in the map
        muscleGroupMap.put("Chest", createMuscleGroupIfNotExists("Chest"));
        muscleGroupMap.put("Back", createMuscleGroupIfNotExists("Back"));
        muscleGroupMap.put("Shoulders", createMuscleGroupIfNotExists("Shoulders"));
        muscleGroupMap.put("Biceps", createMuscleGroupIfNotExists("Biceps"));
        muscleGroupMap.put("Triceps", createMuscleGroupIfNotExists("Triceps"));
        muscleGroupMap.put("Legs", createMuscleGroupIfNotExists("Legs"));
        muscleGroupMap.put("Abs", createMuscleGroupIfNotExists("Abs"));
        muscleGroupMap.put("Forearms", createMuscleGroupIfNotExists("Forearms"));

        // Create exercise templates for each muscle group
        createChestExercises();
        createBackExercises();
        createShoulderExercises();
        createBicepsExercises();
        createTricepsExercises();
        createLegExercises();
        createAbsExercises();
    }

    private MuscleGroup createMuscleGroupIfNotExists(String name) {
        Optional<MuscleGroup> existingGroup = muscleGroupRepository.findByName(name);
        if (existingGroup.isPresent()) {
            return existingGroup.get();
        } else {
            MuscleGroup muscleGroup = new MuscleGroup(name);
            return muscleGroupRepository.save(muscleGroup);
        }
    }

    private void createExerciseTemplateIfNotExists(String name, MuscleGroup primaryMuscleGroup,
            MuscleGroup secondaryMuscleGroup, String description, Boolean requiresWeight) {
        if (!exerciseTemplateRepository.findByName(name).isPresent()) {
            ExerciseTemplate template = new ExerciseTemplate(name, primaryMuscleGroup, description, requiresWeight);
            if (secondaryMuscleGroup != null) {
                template.setSecondaryMuscleGroup(secondaryMuscleGroup);
            }
            exerciseTemplateRepository.save(template);
        }
    }

    private void createChestExercises() {
        MuscleGroup chest = muscleGroupMap.get("Chest");
        MuscleGroup triceps = muscleGroupMap.get("Triceps");
        MuscleGroup shoulders = muscleGroupMap.get("Shoulders");
        MuscleGroup back = muscleGroupMap.get("Back");

        createExerciseTemplateIfNotExists("Bench Press", chest, triceps,
                "Lie on a flat bench and press the weight upward", true);
        createExerciseTemplateIfNotExists("Incline Bench Press", chest, shoulders,
                "Lie on an inclined bench and press the weight upward", true);
        createExerciseTemplateIfNotExists("Decline Bench Press", chest, triceps,
                "Lie on a declined bench and press the weight upward", true);
        createExerciseTemplateIfNotExists("Dumbbell Fly", chest, null,
                "Lie on a bench with dumbbells extended to the sides, then bring them together in an arc", true);
        createExerciseTemplateIfNotExists("Cable Crossover", chest, null,
                "Stand between cable machines and bring the handles together in front of you", true);
        createExerciseTemplateIfNotExists("Push-Up", chest, triceps, "Standard push-up exercise", false);
        createExerciseTemplateIfNotExists("Dips", chest, triceps, "Lower and raise your body using parallel bars",
                false);
        createExerciseTemplateIfNotExists("Pec Deck", chest, null,
                "Sit at the machine and bring the arms together in front of you", true);
        createExerciseTemplateIfNotExists("Landmine Press", chest, shoulders,
                "Press one end of a barbell upward while the other end is secured to the ground", true);
        createExerciseTemplateIfNotExists("Svend Press", chest, null,
                "Press a plate between your palms straight out in front of you", true);
        createExerciseTemplateIfNotExists("Machine Chest Press", chest, triceps,
                "Sit at the machine and press the handles forward", true);
        createExerciseTemplateIfNotExists("Incline Dumbbell Press", chest, shoulders,
                "Lie on an inclined bench and press dumbbells upward", true);
        createExerciseTemplateIfNotExists("Decline Dumbbell Press", chest, triceps,
                "Lie on a declined bench and press dumbbells upward", true);
        createExerciseTemplateIfNotExists("Incline Dumbbell Fly", chest, null,
                "Lie on an inclined bench with dumbbells extended to the sides, then bring them together in an arc",
                true);
        createExerciseTemplateIfNotExists("Decline Dumbbell Fly", chest, null,
                "Lie on a declined bench with dumbbells extended to the sides, then bring them together in an arc",
                true);
        createExerciseTemplateIfNotExists("Cable Fly", chest, null,
                "Using cables, perform fly motions at various angles", true);
        createExerciseTemplateIfNotExists("Smith Machine Bench Press", chest, triceps,
                "Bench press using the Smith machine", true);
        createExerciseTemplateIfNotExists("Close-Grip Bench Press", chest, triceps,
                "Bench press with hands closer together to target triceps more", true);
        createExerciseTemplateIfNotExists("Dumbbell Pullover", chest, back,
                "Lie across a bench with a dumbbell extended over your chest, then lower it behind your head", true);
        createExerciseTemplateIfNotExists("Weighted Push-Up", chest, triceps, "Push-up with added weight on your back",
                true);
    }

    private void createBackExercises() {
        MuscleGroup back = muscleGroupMap.get("Back");
        MuscleGroup biceps = muscleGroupMap.get("Biceps");
        MuscleGroup shoulders = muscleGroupMap.get("Shoulders");

        createExerciseTemplateIfNotExists("Deadlift", back, null,
                "Lift a barbell from the ground to a standing position", true);
        createExerciseTemplateIfNotExists("Pull-Up", back, biceps, "Pull your body upward by gripping an overhead bar",
                false);
        createExerciseTemplateIfNotExists("Lat Pulldown", back, biceps,
                "Pull a bar down to your chest while seated at a machine", true);
        createExerciseTemplateIfNotExists("Barbell Row", back, biceps,
                "Bend over and row a barbell toward your lower chest", true);
        createExerciseTemplateIfNotExists("Dumbbell Row", back, biceps,
                "Place one knee and hand on a bench and row a dumbbell with the other hand", true);
        createExerciseTemplateIfNotExists("T-Bar Row", back, biceps, "Row one end of a barbell loaded with weights",
                true);
        createExerciseTemplateIfNotExists("Cable Row", back, biceps,
                "Sit at a cable machine and pull the handle toward your torso", true);
        createExerciseTemplateIfNotExists("Face Pull", back, shoulders,
                "Pull a rope attachment toward your face at a high cable station", true);
        createExerciseTemplateIfNotExists("Reverse Fly", back, shoulders,
                "Using dumbbells or a machine, pull your arms apart to the sides", true);
        createExerciseTemplateIfNotExists("Good Morning", back, null,
                "With a barbell on your shoulders, bend at your hips until parallel with floor", true);
        createExerciseTemplateIfNotExists("Superman", back, null,
                "Lie face down and lift your arms and legs off the ground", false);
        createExerciseTemplateIfNotExists("Chin-Up", back, biceps, "Pull-up with palms facing toward you", false);
        createExerciseTemplateIfNotExists("Pendlay Row", back, biceps, "Barbell row starting from the floor each rep",
                true);
        createExerciseTemplateIfNotExists("Meadows Row", back, biceps, "One-armed row with a landmine setup", true);
        createExerciseTemplateIfNotExists("Rack Pull", back, null,
                "Deadlift variation that starts from an elevated position", true);
        createExerciseTemplateIfNotExists("Straight-Arm Pulldown", back, null,
                "Keeping your arms straight, pull a high cable downward", true);
        createExerciseTemplateIfNotExists("Smith Machine Row", back, biceps, "Barbell row using the Smith machine",
                true);
        createExerciseTemplateIfNotExists("Machine Row", back, biceps, "Row using a dedicated rowing machine", true);
        createExerciseTemplateIfNotExists("Single-Arm Cable Row", back, biceps, "Cable row performed one arm at a time",
                true);
        createExerciseTemplateIfNotExists("Shrug", back, shoulders,
                "Lift your shoulders toward your ears while holding weights", true);
    }

    private void createShoulderExercises() {
        MuscleGroup shoulders = muscleGroupMap.get("Shoulders");
        MuscleGroup triceps = muscleGroupMap.get("Triceps");
        MuscleGroup back = muscleGroupMap.get("Back");

        createExerciseTemplateIfNotExists("Overhead Press", shoulders, triceps,
                "Press a barbell or dumbbells overhead while standing", true);
        createExerciseTemplateIfNotExists("Seated Shoulder Press", shoulders, triceps,
                "Press a barbell or dumbbells overhead while seated", true);
        createExerciseTemplateIfNotExists("Lateral Raise", shoulders, null,
                "Raise weights out to your sides with a slight bend in the elbows", true);
        createExerciseTemplateIfNotExists("Front Raise", shoulders, null,
                "Raise weights in front of you with arms extended", true);
        createExerciseTemplateIfNotExists("Upright Row", shoulders, back,
                "Pull a barbell or dumbbells up toward your chin", true);
        createExerciseTemplateIfNotExists("Arnold Press", shoulders, triceps,
                "A rotational dumbbell press named after Arnold Schwarzenegger", true);
        createExerciseTemplateIfNotExists("Bent-Over Lateral Raise", shoulders, back,
                "Perform lateral raises while bent at the waist", true);
        createExerciseTemplateIfNotExists("Cable Lateral Raise", shoulders, null,
                "Lateral raise using a low cable pulley", true);
        createExerciseTemplateIfNotExists("Cable Front Raise", shoulders, null, "Front raise using a low cable pulley",
                true);
        createExerciseTemplateIfNotExists("Push Press", shoulders, triceps,
                "Overhead press with a slight leg drive to help initiate the movement", true);
        createExerciseTemplateIfNotExists("Barbell Shrug", shoulders, back,
                "Lift your shoulders toward your ears while holding a barbell", true);
        createExerciseTemplateIfNotExists("Dumbbell Shrug", shoulders, back,
                "Lift your shoulders toward your ears while holding dumbbells", true);
        createExerciseTemplateIfNotExists("Face Pull", shoulders, back,
                "Pull a rope attachment toward your face at a high cable station", true);
        createExerciseTemplateIfNotExists("Behind-the-Neck Press", shoulders, triceps,
                "Press a barbell from behind your neck to overhead", true);
        createExerciseTemplateIfNotExists("Smith Machine Shoulder Press", shoulders, triceps,
                "Shoulder press using the Smith machine", true);
        createExerciseTemplateIfNotExists("Military Press", shoulders, triceps, "Strict overhead press with a barbell",
                true);
        createExerciseTemplateIfNotExists("Machine Shoulder Press", shoulders, triceps,
                "Shoulder press using a machine", true);
        createExerciseTemplateIfNotExists("Cuban Press", shoulders, null,
                "Rotational movement that works the rotator cuff and deltoids", true);
        createExerciseTemplateIfNotExists("Z-Press", shoulders, triceps,
                "Seated overhead press performed sitting on the floor with legs extended", true);
        createExerciseTemplateIfNotExists("Bradford Press", shoulders, triceps,
                "Press that alternates between front and behind-the-neck positions", true);
    }

    private void createBicepsExercises() {
        MuscleGroup biceps = muscleGroupMap.get("Biceps");
        MuscleGroup forearms = muscleGroupMap.get("Forearms");
        MuscleGroup back = muscleGroupMap.get("Back");

        createExerciseTemplateIfNotExists("Barbell Curl", biceps, forearms, "Curl a barbell from a standing position",
                true);
        createExerciseTemplateIfNotExists("Dumbbell Curl", biceps, forearms,
                "Curl dumbbells one at a time or simultaneously", true);
        createExerciseTemplateIfNotExists("Hammer Curl", biceps, forearms,
                "Curl dumbbells with a neutral grip (palms facing each other)", true);
        createExerciseTemplateIfNotExists("Preacher Curl", biceps, forearms,
                "Curl using a preacher bench that supports your arms", true);
        createExerciseTemplateIfNotExists("Cable Curl", biceps, forearms, "Curl using a cable machine", true);
        createExerciseTemplateIfNotExists("Concentration Curl", biceps, null,
                "Seated curl with your elbow braced against your inner thigh", true);
        createExerciseTemplateIfNotExists("EZ Bar Curl", biceps, forearms, "Curl using an EZ curl bar", true);
        createExerciseTemplateIfNotExists("Incline Dumbbell Curl", biceps, forearms,
                "Curl dumbbells while lying back on an inclined bench", true);
        createExerciseTemplateIfNotExists("Spider Curl", biceps, null,
                "Lie face down on an inclined bench and curl a barbell", true);
        createExerciseTemplateIfNotExists("Reverse Curl", biceps, forearms, "Curl with your palms facing down", true);
        createExerciseTemplateIfNotExists("Drag Curl", biceps, null,
                "Curl a barbell while keeping it close to your body", true);
        createExerciseTemplateIfNotExists("Machine Curl", biceps, null, "Curl using a dedicated bicep machine", true);
        createExerciseTemplateIfNotExists("Cable Rope Hammer Curl", biceps, forearms,
                "Hammer curl using a rope attachment on a cable machine", true);
        createExerciseTemplateIfNotExists("Zottman Curl", biceps, forearms,
                "Curl that rotates from a standard to a reverse grip at the top", true);
        createExerciseTemplateIfNotExists("Cross-Body Hammer Curl", biceps, forearms,
                "Hammer curl bringing the dumbbell across your body", true);
        createExerciseTemplateIfNotExists("Bayesian Curl", biceps, null,
                "Curl with elbows positioned slightly behind your body", true);
        createExerciseTemplateIfNotExists("Cheat Curl", biceps, back,
                "Curl using a slight body swing to handle heavier weights", true);
        createExerciseTemplateIfNotExists("Scott Curl", biceps, null, "Another name for preacher curls", true);
        createExerciseTemplateIfNotExists("Chin-Up", biceps, back, "Pull-up with palms facing toward you", false);
        createExerciseTemplateIfNotExists("21s", biceps, null,
                "Partial range-of-motion curl sequence: 7 bottom-half, 7 top-half, 7 full", true);
    }

    private void createTricepsExercises() {
        MuscleGroup triceps = muscleGroupMap.get("Triceps");
        MuscleGroup chest = muscleGroupMap.get("Chest");
        MuscleGroup shoulders = muscleGroupMap.get("Shoulders");

        createExerciseTemplateIfNotExists("Triceps Pushdown", triceps, null,
                "Push a bar or rope down using a high cable pulley", true);
        createExerciseTemplateIfNotExists("Close-Grip Bench Press", triceps, chest,
                "Bench press with hands closer together to target triceps", true);
        createExerciseTemplateIfNotExists("Skullcrusher", triceps, null,
                "Lower a barbell to your forehead while lying on a bench", true);
        createExerciseTemplateIfNotExists("Overhead Triceps Extension", triceps, null,
                "Extend a weight overhead while keeping upper arms stationary", true);
        createExerciseTemplateIfNotExists("Dip", triceps, chest, "Lower and raise your body using parallel bars",
                false);
        createExerciseTemplateIfNotExists("Diamond Push-Up", triceps, chest,
                "Push-up with hands close together forming a diamond shape", false);
        createExerciseTemplateIfNotExists("Bench Dip", triceps, null, "Dip between two benches with feet elevated",
                false);
        createExerciseTemplateIfNotExists("Rope Pushdown", triceps, null, "Triceps pushdown using a rope attachment",
                true);
        createExerciseTemplateIfNotExists("V-Bar Pushdown", triceps, null, "Triceps pushdown using a V-shaped bar",
                true);
        createExerciseTemplateIfNotExists("Kickback", triceps, null,
                "Bend over and extend your arm behind you while holding a weight", true);
        createExerciseTemplateIfNotExists("JM Press", triceps, chest,
                "A hybrid of close-grip bench press and skullcrusher", true);
        createExerciseTemplateIfNotExists("French Press", triceps, null, "Another name for overhead triceps extension",
                true);
        createExerciseTemplateIfNotExists("Tate Press", triceps, null,
                "Lie on a bench and lower dumbbells to your chest with elbows out", true);
        createExerciseTemplateIfNotExists("Triceps Extension Machine", triceps, null,
                "Extension using a dedicated triceps machine", true);
        createExerciseTemplateIfNotExists("Lying Triceps Extension", triceps, null,
                "Extend a barbell or dumbbells while lying on a bench", true);
        createExerciseTemplateIfNotExists("Close-Grip Push-Up", triceps, chest,
                "Push-up with hands placed closer than shoulder width", false);
        createExerciseTemplateIfNotExists("Cable Overhead Extension", triceps, null,
                "Overhead extension using a cable machine", true);
        createExerciseTemplateIfNotExists("Smith Machine Close-Grip Bench", triceps, chest,
                "Close-grip bench press using the Smith machine", true);
        createExerciseTemplateIfNotExists("Rolling Triceps Extension", triceps, null,
                "Skullcrusher that 'rolls' the weight back over your head at the bottom", true);
        createExerciseTemplateIfNotExists("Board Press", triceps, chest,
                "Bench press to boards placed on chest to limit range of motion", true);
    }

    private void createLegExercises() {
        MuscleGroup legs = muscleGroupMap.get("Legs");

        createExerciseTemplateIfNotExists("Squat", legs, null,
                "Bend your knees and lower your body while keeping the back straight", true);
        createExerciseTemplateIfNotExists("Deadlift", legs, null,
                "Lift a barbell from the ground to a standing position", true);
        createExerciseTemplateIfNotExists("Leg Press", legs, null,
                "Push a weighted platform away from you using your legs", true);
        createExerciseTemplateIfNotExists("Leg Extension", legs, null,
                "Extend your legs using a machine that targets the quadriceps", true);
        createExerciseTemplateIfNotExists("Leg Curl", legs, null,
                "Curl your legs using a machine that targets the hamstrings", true);
        createExerciseTemplateIfNotExists("Lunge", legs, null,
                "Step forward and lower your body until both knees are bent at 90 degrees", true);
        createExerciseTemplateIfNotExists("Bulgarian Split Squat", legs, null,
                "Lunge with your rear foot elevated on a bench", true);
        createExerciseTemplateIfNotExists("Calf Raise", legs, null,
                "Raise your heels off the ground to target your calves", true);
        createExerciseTemplateIfNotExists("Hack Squat", legs, null, "Squat using a machine with your back supported",
                true);
        createExerciseTemplateIfNotExists("Romanian Deadlift", legs, null,
                "Deadlift variation that targets the hamstrings", true);
        createExerciseTemplateIfNotExists("Front Squat", legs, null,
                "Squat with the barbell held in front of your shoulders", true);
        createExerciseTemplateIfNotExists("Goblet Squat", legs, null,
                "Squat while holding a dumbbell or kettlebell at your chest", true);
        createExerciseTemplateIfNotExists("Step-Up", legs, null,
                "Step up onto a raised platform with one leg at a time", true);
        createExerciseTemplateIfNotExists("Box Jump", legs, null, "Jump onto a raised platform with both feet", false);
        createExerciseTemplateIfNotExists("Walking Lunge", legs, null, "Lunge continuously walking forward", true);
        createExerciseTemplateIfNotExists("Good Morning", legs, null,
                "With a barbell on your shoulders, bend at your hips until parallel with floor", true);
        createExerciseTemplateIfNotExists("Hip Thrust", legs, null,
                "Thrust your hips upward while your upper back is supported on a bench", true);
        createExerciseTemplateIfNotExists("Sumo Deadlift", legs, null,
                "Deadlift with a wide stance and hands inside knees", true);
        createExerciseTemplateIfNotExists("Seated Calf Raise", legs, null, "Calf raise performed in a seated position",
                true);
        createExerciseTemplateIfNotExists("Leg Press Calf Raise", legs, null,
                "Calf raise performed on a leg press machine", true);
    }

    private void createAbsExercises() {
        MuscleGroup abs = muscleGroupMap.get("Abs");

        createExerciseTemplateIfNotExists("Crunch", abs, null,
                "Lie on your back and curl your shoulders toward your hips", false);
        createExerciseTemplateIfNotExists("Leg Raise", abs, null,
                "Lie on your back and raise your legs toward the ceiling", false);
        createExerciseTemplateIfNotExists("Plank", abs, null,
                "Hold a push-up position with your body in a straight line", false);
        createExerciseTemplateIfNotExists("Russian Twist", abs, null,
                "Sit with knees bent and twist your torso from side to side", true);
        createExerciseTemplateIfNotExists("Bicycle Crunch", abs, null,
                "Lie on your back and bring opposite elbow to knee in a pedaling motion", false);
        createExerciseTemplateIfNotExists("Mountain Climber", abs, null,
                "In a plank position, bring knees toward chest alternately", false);
        createExerciseTemplateIfNotExists("Ab Rollout", abs, null, "Use an ab wheel to roll forward and back", false);
        createExerciseTemplateIfNotExists("Hanging Leg Raise", abs, null, "Hang from a bar and raise your legs", false);
        createExerciseTemplateIfNotExists("Cable Crunch", abs, null,
                "Kneel in front of a cable machine and crunch downward", true);
        createExerciseTemplateIfNotExists("Decline Sit-Up", abs, null, "Sit-up performed on a declined bench", false);
        createExerciseTemplateIfNotExists("Dragon Flag", abs, null,
                "Lie on a bench and raise and lower your straight body", false);
        createExerciseTemplateIfNotExists("Windshield Wiper", abs, null,
                "Lie on your back with legs up and rotate them side to side", false);
        createExerciseTemplateIfNotExists("Side Plank", abs, null, "Plank performed on one side of your body", false);
        createExerciseTemplateIfNotExists("Flutter Kick", abs, null,
                "Lie on your back and make small, rapid up and down kicks", false);
        createExerciseTemplateIfNotExists("Hollow Hold", abs, null,
                "Lie on your back and hold your arms and legs off the ground", false);
        createExerciseTemplateIfNotExists("L-Sit", abs, null,
                "Hold your body off the ground with legs extended parallel to the floor", false);
        createExerciseTemplateIfNotExists("Toe Touch", abs, null,
                "Lie on your back with legs up and reach for your toes", false);
        createExerciseTemplateIfNotExists("Scissor Kick", abs, null,
                "Lie on your back and cross one leg over the other repeatedly", false);
        createExerciseTemplateIfNotExists("Reverse Crunch", abs, null,
                "Lie on your back and bring your knees toward your chest", false);
        createExerciseTemplateIfNotExists("Ab Machine Crunch", abs, null, "Crunch using a dedicated abdominal machine",
                true);
    }
}