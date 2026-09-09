package com.hiretrack.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PlacementDTO {
    private UUID id;
    private UUID studentId;
    private UUID companyId;
    private UUID driveId;
    private java.math.BigDecimal packageOffered;
    private String jobRole;
    private java.time.LocalDate placementDate;
    private UUID departmentId;
    private Integer batch;
    private LocalDateTime createdAt;
}
