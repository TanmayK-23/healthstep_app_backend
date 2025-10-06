package com.healthstep.mobile.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.healthstep.mobile.model.WaterLog;

import java.time.LocalDate;
import java.util.List;

public interface WaterLogRepository extends JpaRepository<WaterLog, Long> {
    List<WaterLog> findByUserIdAndDate(Long userId, LocalDate date);
}