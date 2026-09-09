package com.hiretrack.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ReportDTO {
    private UUID id;
    private UUID generatedById;
    private String reportType;
    private String dataUrl;
    private LocalDateTime createdAt;
}
