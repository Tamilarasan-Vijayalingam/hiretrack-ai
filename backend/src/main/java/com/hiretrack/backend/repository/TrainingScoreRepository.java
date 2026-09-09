package com.hiretrack.backend.repository;

import com.hiretrack.backend.entity.TrainingScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface TrainingScoreRepository extends JpaRepository<TrainingScore, UUID> {
    java.util.List<TrainingScore> findByStudentId(UUID studentId);
}

