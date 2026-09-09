package com.hiretrack.backend.dto.analytics;

import lombok.Data;

@Data
public class TrainingAnalyticsDTO {
    private String programName;
    private double beforeAptitude;
    private double afterAptitude;
    private double aptitudeImprovementPercentage;
    
    private double beforeCoding;
    private double afterCoding;
    private double codingImprovementPercentage;
    
    private double beforeInterview;
    private double afterInterview;
    private double interviewImprovementPercentage;
}
