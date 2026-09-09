package com.hiretrack.backend.repository;

import com.hiretrack.backend.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {
    Optional<Company> findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
    List<Company> findByHiringStatus(String hiringStatus);
    List<Company> findByIndustryIgnoreCase(String industry);
}
