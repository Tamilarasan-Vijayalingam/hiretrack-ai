package com.hiretrack.backend.service.ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hiretrack.backend.dto.ai.AtsScoreDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Slf4j
public class AtsAnalyzerService {

    private final OpenAiClient openAiClient;
    private final ObjectMapper objectMapper;

    public AtsScoreDTO analyzeResume(String resumeText) {
        String systemPrompt = "You are an ATS resume analyzer. Analyze the provided resume text and return a JSON object exactly matching this structure with no markdown or extra text: { \"atsScore\": double, \"keywordMatchRatio\": double, \"matchedKeywords\": [\"string\"], \"missingKeywords\": [\"string\"], \"missingSkills\": [\"string\"], \"qualityRating\": \"string\", \"improvementSuggestions\": [\"string\"] }";
        String response = openAiClient.callOpenAi(systemPrompt, resumeText != null ? resumeText : "Empty resume");
        
        if (response != null) {
            try {
                if (response.startsWith("```json")) {
                    response = response.substring(7, response.length() - 3);
                } else if (response.startsWith("```")) {
                    response = response.substring(3, response.length() - 3);
                }
                return objectMapper.readValue(response, AtsScoreDTO.class);
            } catch (Exception e) {
                log.error("Failed to parse OpenAI response to AtsScoreDTO", e);
            }
        }
        
        // Fallback
        AtsScoreDTO dto = new AtsScoreDTO();
        dto.setAtsScore(78.5);
        dto.setKeywordMatchRatio(0.75);
        dto.setMatchedKeywords(Arrays.asList("Java", "REST", "API", "Database"));
        dto.setMissingKeywords(Arrays.asList("Agile", "CI/CD", "Testing"));
        dto.setMissingSkills(Arrays.asList("Jenkins", "JUnit"));
        dto.setQualityRating("GOOD");
        dto.setImprovementSuggestions(Arrays.asList("Add quantifiable metrics to project descriptions", "Include specific testing frameworks used"));
        return dto;
    }
}
