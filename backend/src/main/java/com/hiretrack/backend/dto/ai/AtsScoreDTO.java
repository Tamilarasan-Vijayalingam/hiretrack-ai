package com.hiretrack.backend.dto.ai;

import lombok.Data;
import java.util.List;

@Data
public class AtsScoreDTO {
    private double atsScore;
    private double keywordMatchRatio;
    private List<String> matchedKeywords;
    private List<String> missingKeywords;
    private List<String> missingSkills;
    private String qualityRating;
    private List<String> improvementSuggestions;
}
