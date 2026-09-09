package com.hiretrack.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AiPlacementReadinessDTO {
    private UUID id;
    private UUID studentId;
    private Integer score;
    private String readinessBreakdown;
    private LocalDateTime updatedAt;
}
