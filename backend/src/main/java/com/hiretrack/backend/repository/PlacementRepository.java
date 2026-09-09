package com.hiretrack.backend.repository;

import com.hiretrack.backend.entity.Placement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PlacementRepository extends JpaRepository<Placement, UUID> {
    List<Placement> findByCompanyId(UUID companyId);
}
