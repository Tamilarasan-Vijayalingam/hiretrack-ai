package com.hiretrack.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class DriveDTO {
    private UUID id;
    private UUID companyId;
    private String title;
    private String jobRole;
    private java.time.LocalDate driveDate;
    private java.time.LocalDate registrationDeadline;
    private java.time.LocalDate testDate;
    private java.time.LocalDate interviewDate;
    private String packageOffered;
    private java.math.BigDecimal minCgpa;
    private Integer maxBacklogs;
    private String eligibilityCriteria;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
