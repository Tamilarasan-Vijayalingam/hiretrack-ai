package com.hiretrack.backend.repository;

import com.hiretrack.backend.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, UUID> {
    java.util.List<Resume> findByStudentIdOrderByUploadedAtDesc(UUID studentId);
}

