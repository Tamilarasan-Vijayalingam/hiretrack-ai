package com.hiretrack.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class StudentDTO {
    private UUID id;
    private UUID userId;
    private String name;
    private String registerNumber;
    private String email;
    private String phone;
    private UUID departmentId;
    private java.math.BigDecimal cgpa;
    private Integer backlogs;
    private String certifications;
    private String projects;
    private Integer profileCompletion;
    private Integer graduationYear;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
