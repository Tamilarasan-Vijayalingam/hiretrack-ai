package com.hiretrack.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class DriveApplicationDTO {
    private UUID id;
    private UUID studentId;
    private UUID driveId;
    private String status;
    private LocalDateTime appliedAt;
}
