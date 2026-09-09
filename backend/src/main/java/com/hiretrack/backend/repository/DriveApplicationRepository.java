package com.hiretrack.backend.repository;

import com.hiretrack.backend.entity.DriveApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DriveApplicationRepository extends JpaRepository<DriveApplication, UUID> {
    List<DriveApplication> findByDriveId(UUID driveId);
}
