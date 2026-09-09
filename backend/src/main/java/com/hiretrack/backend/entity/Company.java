package com.hiretrack.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "companies")
public class Company {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true, nullable = false)
    private String name;

    private String industry;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "hiring_criteria", columnDefinition = "TEXT")
    private String hiringCriteria;

    private String website;

    private String location;

    @Column(name = "hr_name")
    private String hrName;

    @Column(name = "hr_email")
    private String hrEmail;

    @Column(name = "hr_phone")
    private String hrPhone;

    @Column(name = "job_role")
    private String jobRole;

    @Column(name = "package_ctc")
    private String packageCtc;

    @Column(name = "min_cgpa", precision = 4, scale = 2)
    private BigDecimal minCgpa;

    @Column(name = "max_backlogs")
    private Integer maxBacklogs;

    @Column(name = "required_skills", columnDefinition = "TEXT")
    private String requiredSkills;

    @Column(name = "required_certifications", columnDefinition = "TEXT")
    private String requiredCertifications;

    private Integer openings;

    @Column(name = "hiring_status")
    private String hiringStatus = "ACTIVE";

    @Column(name = "drive_date")
    private LocalDate driveDate;

    @Column(name = "registration_deadline")
    private LocalDate registrationDeadline;

    @Column(name = "selection_process", columnDefinition = "TEXT")
    private String selectionProcess;

    @Column(name = "logo_url")
    private String logoUrl;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
