package com.hiretrack.backend.repository;

import com.hiretrack.backend.entity.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, UUID> {
    java.util.List<Interview> findByStudentIdOrderByCreatedAtDesc(UUID studentId);
}

