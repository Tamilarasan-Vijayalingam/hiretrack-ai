package com.hiretrack.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ResumeDTO {
    private UUID id;
    private UUID studentId;
    private Integer atsScore;
    private String missingKeywords;
    private String missingSkills;
    private String feedback;
    private LocalDateTime uploadedAt;
}
