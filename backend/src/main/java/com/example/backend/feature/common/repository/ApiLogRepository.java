package com.example.backend.feature.common.repository;

import com.example.backend.feature.common.entity.ApiLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiLogRepository extends JpaRepository<ApiLog, Long> {
}