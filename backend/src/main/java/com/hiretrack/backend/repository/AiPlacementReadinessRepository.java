package com.hiretrack.backend.repository;

import com.hiretrack.backend.entity.AiPlacementReadiness;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface AiPlacementReadinessRepository extends JpaRepository<AiPlacementReadiness, UUID> {
    java.util.Optional<AiPlacementReadiness> findByStudentId(UUID studentId);
}

