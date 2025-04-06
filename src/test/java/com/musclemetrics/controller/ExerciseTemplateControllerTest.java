package com.musclemetrics.controller;

import com.musclemetrics.dto.exercise.ExerciseTemplateResponse;
import com.musclemetrics.model.ExerciseTemplate;
import com.musclemetrics.model.MuscleGroup;
import com.musclemetrics.service.ExerciseTemplateService;
import com.musclemetrics.service.MuscleGroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ExerciseTemplateControllerTest {

    @Mock
    private ExerciseTemplateService exerciseTemplateService;

    @Mock
    private MuscleGroupService muscleGroupService;

    @InjectMocks
    private ExerciseTemplateController exerciseTemplateController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetPublicExerciseTemplatesContainsMuscleGroups() {
        // Create test data
        MuscleGroup chest = new MuscleGroup();
        chest.setId("chest");
        chest.setName("Chest");

        MuscleGroup triceps = new MuscleGroup();
        triceps.setId("triceps");
        triceps.setName("Triceps");

        ExerciseTemplate benchPress = new ExerciseTemplate();
        benchPress.setId("bench-press");
        benchPress.setName("Bench Press");
        benchPress.setRequiresWeight(true);
        benchPress.setPrimaryMuscleGroup(chest);
        benchPress.setSecondaryMuscleGroup(triceps);

        // Mock service behavior
        when(exerciseTemplateService.findAll()).thenReturn(Arrays.asList(benchPress));

        // Call the controller method
        ResponseEntity<List<ExerciseTemplateResponse>> response = exerciseTemplateController
                .getPublicExerciseTemplates();

        // Verify that the response contains the expected data
        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<ExerciseTemplateResponse> templates = response.getBody();
        assertNotNull(templates);
        assertEquals(1, templates.size());

        ExerciseTemplateResponse template = templates.get(0);
        assertEquals("bench-press", template.getId());
        assertEquals("Bench Press", template.getName());
        assertTrue(template.isRequiresWeight());

        // Verify muscleGroups contains both primary and secondary muscle groups
        assertNotNull(template.getMuscleGroups());
        assertEquals(2, template.getMuscleGroups().size());

        // Check first muscle group
        assertEquals("chest", template.getMuscleGroups().get(0).getId());
        assertEquals("Chest", template.getMuscleGroups().get(0).getName());

        // Check second muscle group
        assertEquals("triceps", template.getMuscleGroups().get(1).getId());
        assertEquals("Triceps", template.getMuscleGroups().get(1).getName());
    }
}