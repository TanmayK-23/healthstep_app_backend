package com.healthstep.mobile.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.healthstep.mobile.model.WorkoutLog;

import java.time.LocalDate;
import java.util.List;

public interface WorkoutLogRepository extends JpaRepository<WorkoutLog, Long> {
    List<WorkoutLog> findByUserIdAndDate(Long userId, LocalDate date);
}