package com.example.backend.feature.common.repository;

import com.example.backend.feature.common.entity.ApiLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ApiLogRepository extends JpaRepository<ApiLog, Long> {

    @Query("SELECT al.procMs FROM ApiLog al WHERE al.modelNm = :modelName ORDER BY al.id DESC LIMIT 1")
    Integer findLastProcMs(@Param("modelName") String modelName);
}