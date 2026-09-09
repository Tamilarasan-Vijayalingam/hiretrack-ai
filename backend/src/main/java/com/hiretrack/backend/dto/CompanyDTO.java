package com.hiretrack.backend.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class CompanyDTO {
    private UUID id;

    @NotBlank(message = "Company name is required")
    private String name;

    @NotBlank(message = "Industry is required")
    private String industry;

    private String description;
    private String hiringCriteria;
    private String website;
    private String location;

    private String hrName;

    @Email(message = "HR email must be a valid email address")
    private String hrEmail;

    private String hrPhone;

    @NotBlank(message = "Job role is required")
    private String jobRole;

    @NotBlank(message = "Package/CTC is required")
    private String packageCtc;

    @DecimalMin(value = "0.0", message = "CGPA must be at least 0.0")
    @DecimalMax(value = "10.0", message = "CGPA cannot exceed 10.0")
    private BigDecimal minCgpa;

    private Integer maxBacklogs;
    private String requiredSkills;
    private String requiredCertifications;
    private Integer openings;
    private String hiringStatus = "ACTIVE"; // ACTIVE, UPCOMING, CLOSED
    private LocalDate driveDate;
    private LocalDate registrationDeadline;
    private String selectionProcess;
    private String logoUrl;

    // Computed / aggregated statistics
    private Integer eligibleStudentCount;
    private Integer appliedCount;
    private Integer shortlistedCount;
    private Integer selectedCount;
    private Double selectionPercentage;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
