package com.healthstep.mobile.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.healthstep.mobile.model.NutritionLog;

import java.time.LocalDate;
import java.util.List;

public interface NutritionLogRepository extends JpaRepository<NutritionLog, Long> {
    List<NutritionLog> findByUserIdAndDate(Long userId, LocalDate date);
    void deleteByUserIdAndDate(Long userId, LocalDate date);
}