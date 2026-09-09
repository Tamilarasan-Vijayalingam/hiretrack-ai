package com.hiretrack.backend.service.ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hiretrack.backend.dto.ai.SkillsGapDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Slf4j
public class SkillsGapService {

    private final OpenAiClient openAiClient;
    private final ObjectMapper objectMapper;

    public SkillsGapDTO analyzeGap(String targetRole) {
        String systemPrompt = "You are a career advisor. For the given target role, return a JSON object exactly matching this structure with no markdown: { \"matchingSkills\": [\"string\"], \"missingSkills\": [\"string\"], \"recommendedCourses\": [\"string\"], \"learningRoadmap\": [\"string\"] }. Base matching skills on a typical Computer Science student's baseline.";
        String response = openAiClient.callOpenAi(systemPrompt, "Target role: " + targetRole);
        
        if (response != null) {
            try {
                if (response.startsWith("```json")) {
                    response = response.substring(7, response.length() - 3);
                } else if (response.startsWith("```")) {
                    response = response.substring(3, response.length() - 3);
                }
                return objectMapper.readValue(response, SkillsGapDTO.class);
            } catch (Exception e) {
                log.error("Failed to parse OpenAI response to SkillsGapDTO", e);
            }
        }
        
        // Fallback
        SkillsGapDTO dto = new SkillsGapDTO();
        dto.setMatchingSkills(Arrays.asList("Java", "Spring Boot", "SQL"));
        if (targetRole != null && targetRole.toLowerCase().contains("frontend")) {
            dto.setMissingSkills(Arrays.asList("React", "TypeScript"));
            dto.setRecommendedCourses(Arrays.asList("Advanced React Patterns", "TypeScript for Java Developers"));
            dto.setLearningRoadmap(Arrays.asList("Week 1: JS Basics", "Week 2-3: React Hooks", "Week 4: TS Integration"));
        } else {
            dto.setMissingSkills(Arrays.asList("AWS", "Docker", "System Design"));
            dto.setRecommendedCourses(Arrays.asList("AWS Solutions Architect Associate", "Docker for Microservices"));
            dto.setLearningRoadmap(Arrays.asList("Week 1: Containerization", "Week 2: Cloud Basics", "Week 3: System Architecture"));
        }
        return dto;
    }
}
