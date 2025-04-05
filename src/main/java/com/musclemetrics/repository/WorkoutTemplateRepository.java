package com.musclemetrics.repository;

import com.musclemetrics.model.WorkoutTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkoutTemplateRepository extends MongoRepository<WorkoutTemplate, String> {
    List<WorkoutTemplate> findByFeatured(boolean featured);

    List<WorkoutTemplate> findByCreatedBy(String userId);

    List<WorkoutTemplate> findByType(String type);
}