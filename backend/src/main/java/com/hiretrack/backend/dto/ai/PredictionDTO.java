package com.hiretrack.backend.dto.ai;

import lombok.Data;
import java.util.List;

@Data
public class PredictionDTO {
    private double placementProbability; // 0-100
    private double confidenceScore;
    private String predictedSalaryRange;
    private List<String> strengths;
    private List<String> weaknesses;
    private List<String> improvementSuggestions;
}
