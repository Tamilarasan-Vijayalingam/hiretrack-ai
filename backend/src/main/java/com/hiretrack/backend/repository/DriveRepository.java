package com.hiretrack.backend.repository;

import com.hiretrack.backend.entity.Drive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DriveRepository extends JpaRepository<Drive, UUID> {
    List<Drive> findByCompanyId(UUID companyId);
    Optional<Drive> findFirstByCompanyIdOrderByDriveDateDesc(UUID companyId);
}
