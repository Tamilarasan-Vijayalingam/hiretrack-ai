package com.hiretrack.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class SkillsGapAnalysisDTO {
    private UUID id;
    private UUID studentId;
    private String targetRole;
    private String missingSkills;
    private String recommendedCourses;
    private String learningRoadmap;
    private LocalDateTime updatedAt;
}
