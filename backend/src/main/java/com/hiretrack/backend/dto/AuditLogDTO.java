package com.hiretrack.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AuditLogDTO {
    private UUID id;
    private UUID userId;
    private String action;
    private String entity;
    private UUID entityId;
    private LocalDateTime timestamp;
}
