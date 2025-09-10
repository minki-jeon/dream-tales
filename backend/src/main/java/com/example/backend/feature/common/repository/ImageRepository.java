package com.example.backend.feature.common.repository;

import com.example.backend.feature.common.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}