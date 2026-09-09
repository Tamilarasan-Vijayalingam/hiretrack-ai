package com.hiretrack.backend.repository;

import com.hiretrack.backend.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface ReportRepository extends JpaRepository<Report, UUID> {
}
