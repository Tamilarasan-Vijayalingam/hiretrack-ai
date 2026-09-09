package com.hiretrack.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TrainingScoreDTO {
    private UUID id;
    private UUID studentId;
    private UUID trainingProgramId;
    private Integer beforeAptitude;
    private Integer afterAptitude;
    private Integer beforeCoding;
    private Integer afterCoding;
    private Integer beforeInterview;
    private Integer afterInterview;
    private java.math.BigDecimal improvementPercentage;
}
