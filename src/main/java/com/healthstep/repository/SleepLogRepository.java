package com.healthstep.mobile.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.healthstep.mobile.model.SleepLog;

import java.time.LocalDate;
import java.util.List;

public interface SleepLogRepository extends JpaRepository<SleepLog, Long> {
    List<SleepLog> findByUserIdAndDate(Long userId, LocalDate date);
}