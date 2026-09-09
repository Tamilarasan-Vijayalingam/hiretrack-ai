package com.hiretrack.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "placements")
public class Placement {
    @Id @GeneratedValue
    private UUID id;
    @ManyToOne @JoinColumn(name = "student_id")
    private Student student;
    @ManyToOne @JoinColumn(name = "company_id")
    private Company company;
    @ManyToOne @JoinColumn(name = "drive_id")
    private Drive drive;
    @Column(name = "package_lpa")
    private java.math.BigDecimal packageLpa;
    @Column(name = "package_offered")
    private java.math.BigDecimal packageOffered;
    @Column(name = "job_role", nullable = false)
    private String jobRole;
    @Column(name = "offer_date")
    private java.time.LocalDate offerDate;
    @Column(name = "placement_date", nullable = false)
    private java.time.LocalDate placementDate;
    @ManyToOne @JoinColumn(name = "department_id")
    private Department department;
    @Column(nullable = false)
    private Integer batch;
    @CreationTimestamp
    private LocalDateTime createdAt;
}
