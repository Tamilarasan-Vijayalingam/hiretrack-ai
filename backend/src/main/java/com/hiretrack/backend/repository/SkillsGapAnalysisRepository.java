package com.hiretrack.backend.repository;

import com.hiretrack.backend.entity.SkillsGapAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface SkillsGapAnalysisRepository extends JpaRepository<SkillsGapAnalysis, UUID> {
    java.util.List<SkillsGapAnalysis> findByStudentIdOrderByUpdatedAtDesc(UUID studentId);
    java.util.Optional<SkillsGapAnalysis> findFirstByStudentIdOrderByUpdatedAtDesc(UUID studentId);
}

