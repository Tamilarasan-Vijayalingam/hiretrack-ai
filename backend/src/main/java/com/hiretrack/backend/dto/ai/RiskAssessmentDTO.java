package com.hiretrack.backend.dto.ai;

import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
public class RiskAssessmentDTO {
    private UUID studentId;
    private String studentName;
    private String riskLevel; // LOW, MEDIUM, HIGH, CRITICAL
    private List<String> riskFactors;
    private String interventionRecommendation;
}
