package com.hiretrack.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AiPlacementPredictionDTO {
    private UUID id;
    private UUID studentId;
    private java.math.BigDecimal successProbability;
    private java.math.BigDecimal confidence;
    private String strengths;
    private String weaknesses;
    private String improvementSuggestions;
    private LocalDateTime updatedAt;
}
