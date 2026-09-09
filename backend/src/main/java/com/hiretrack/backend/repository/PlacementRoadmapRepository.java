package com.hiretrack.backend.repository;

import com.hiretrack.backend.entity.PlacementRoadmap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlacementRoadmapRepository extends JpaRepository<PlacementRoadmap, UUID> {
    Optional<PlacementRoadmap> findByStudentId(UUID studentId);
}
