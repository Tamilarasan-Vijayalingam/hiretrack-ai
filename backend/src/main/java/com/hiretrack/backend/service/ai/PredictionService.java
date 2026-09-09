package com.hiretrack.backend.service.ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hiretrack.backend.dto.ai.PredictionDTO;
import com.hiretrack.backend.dto.ai.ReadinessScoreDTO;
import com.hiretrack.backend.entity.Student;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Slf4j
public class PredictionService {

    private final OpenAiClient openAiClient;
    private final ObjectMapper objectMapper;

    public PredictionDTO predictPlacement(Student student, ReadinessScoreDTO readiness) {
        String systemPrompt = "You are a Placement Prediction AI. Based on the student's metrics, predict their placement outcomes. Return a JSON object exactly matching this structure with no markdown: { \"placementProbability\": double, \"confidenceScore\": double, \"predictedSalaryRange\": \"string (e.g. 8-12 LPA)\", \"strengths\": [\"string\"], \"weaknesses\": [\"string\"], \"improvementSuggestions\": [\"string\"] }";
        String userPrompt = String.format("Student: %s, CGPA: %s, Backlogs: %s, Readiness Score: %s", 
            student.getName() != null ? student.getName() : "Unknown",
            student.getCgpa(), student.getBacklogs(), readiness.getOverallScore());

        String response = openAiClient.callOpenAi(systemPrompt, userPrompt);
        
        if (response != null) {
            try {
                if (response.startsWith("```json")) {
                    response = response.substring(7, response.length() - 3);
                } else if (response.startsWith("```")) {
                    response = response.substring(3, response.length() - 3);
                }
                return objectMapper.readValue(response, PredictionDTO.class);
            } catch (Exception e) {
                log.error("Failed to parse OpenAI response to PredictionDTO", e);
            }
        }
        
        // Fallback
        PredictionDTO dto = new PredictionDTO();
        double score = readiness.getOverallScore();
        if (score > 85) {
            dto.setPlacementProbability(95.0);
            dto.setConfidenceScore(90.0);
            dto.setPredictedSalaryRange("10 - 15 LPA");
            dto.setStrengths(Arrays.asList("Excellent Academics", "Strong Technical Skills"));
            dto.setImprovementSuggestions(Arrays.asList("Focus on Advanced System Design for Product Companies"));
        } else if (score > 65) {
            dto.setPlacementProbability(75.0);
            dto.setConfidenceScore(85.0);
            dto.setPredictedSalaryRange("5 - 8 LPA");
            dto.setWeaknesses(Arrays.asList("Average Interview Score"));
            dto.setImprovementSuggestions(Arrays.asList("Practice Mock Interviews", "Build more independent projects"));
        } else {
            dto.setPlacementProbability(40.0);
            dto.setConfidenceScore(70.0);
            dto.setPredictedSalaryRange("3 - 5 LPA");
            dto.setWeaknesses(Arrays.asList("Low Academic Score", "Missing Core Skills"));
            dto.setImprovementSuggestions(Arrays.asList("Clear pending backlogs", "Complete foundation certification courses"));
        }
        return dto;
    }
}
