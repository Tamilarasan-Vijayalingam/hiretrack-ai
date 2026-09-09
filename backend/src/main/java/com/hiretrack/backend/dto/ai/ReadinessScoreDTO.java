package com.hiretrack.backend.dto.ai;

import lombok.Data;

@Data
public class ReadinessScoreDTO {
    private double overallScore; // 0-100
    private double academicScore;
    private double skillsScore;
    private double projectsScore;
    private double certificationsScore;
    private double resumeScore;
    private double interviewScore;
    private String feedback;
}
