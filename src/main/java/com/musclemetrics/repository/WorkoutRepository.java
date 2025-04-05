package com.musclemetrics.repository;

import com.musclemetrics.model.MuscleGroup;
import com.musclemetrics.model.User;
import com.musclemetrics.model.Workout;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface WorkoutRepository extends MongoRepository<Workout, String> {
    List<Workout> findByUserId(String userId);

    List<Workout> findByUserIdAndDateBetween(String userId, LocalDate startDate, LocalDate endDate);

    List<Workout> findByUserIdOrderByDateDesc(String userId);

    List<Workout> findByUser(User user);

    List<Workout> findByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate);

    List<Workout> findByUserAndDayOfWeek(User user, DayOfWeek dayOfWeek);

    List<Workout> findByUserAndTargetMuscleGroupsContains(User user, MuscleGroup muscleGroup);
}