package com.hiretrack.backend.repository;

import com.hiretrack.backend.entity.AiPlacementPrediction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface AiPlacementPredictionRepository extends JpaRepository<AiPlacementPrediction, UUID> {
    java.util.Optional<AiPlacementPrediction> findByStudentId(UUID studentId);
}

