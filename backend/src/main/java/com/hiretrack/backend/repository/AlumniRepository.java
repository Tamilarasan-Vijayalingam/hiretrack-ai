package com.hiretrack.backend.repository;

import com.hiretrack.backend.entity.Alumni;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface AlumniRepository extends JpaRepository<Alumni, UUID> {
}
