package com.hiretrack.backend.dto.ai;

import lombok.Data;
import java.util.List;

@Data
public class SkillsGapDTO {
    private List<String> matchingSkills;
    private List<String> missingSkills;
    private List<String> recommendedCourses;
    private List<String> learningRoadmap;
}
