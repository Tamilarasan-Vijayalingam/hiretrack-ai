package com.hiretrack.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "drives")
public class Drive {
    @Id @GeneratedValue
    private UUID id;
    @ManyToOne @JoinColumn(name = "company_id")
    private Company company;
    @Column(nullable = false)
    private String title;
    @Column(name = "job_role", nullable = false)
    private String jobRole;
    @Column(name = "drive_date", nullable = false)
    private java.time.LocalDate driveDate;
    @Column(name = "registration_deadline", nullable = false)
    private java.time.LocalDate registrationDeadline;
    @Column(name = "test_date")
    private java.time.LocalDate testDate;
    @Column(name = "interview_date")
    private java.time.LocalDate interviewDate;
    @Column(name = "package", nullable = false)
    private String packageOffered;
    @Column(name = "min_cgpa")
    private java.math.BigDecimal minCgpa;
    @Column(name = "max_backlogs")
    private Integer maxBacklogs;
    @Column(name = "eligibility_criteria")
    private String eligibilityCriteria;
    @Enumerated(EnumType.STRING)
    @org.hibernate.annotations.JdbcType(org.hibernate.dialect.PostgreSQLEnumJdbcType.class)
    @Column(nullable = false)
    private DriveStatus status;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
