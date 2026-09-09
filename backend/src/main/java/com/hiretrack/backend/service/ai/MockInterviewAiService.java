package com.hiretrack.backend.service.ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hiretrack.backend.dto.ai.MockInterviewFeedbackDTO;
import com.hiretrack.backend.dto.ai.MockInterviewQuestionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Slf4j
public class MockInterviewAiService {

    private final OpenAiClient openAiClient;
    private final ObjectMapper objectMapper;

    public MockInterviewQuestionDTO generateQuestion(String topic, String type) {
        String systemPrompt = "You are an expert technical interviewer. Generate ONE highly relevant interview question for a college student based on the topic and type provided. Return ONLY a JSON object: { \"question\": \"the question string\", \"category\": \"the category\" }";
        String userPrompt = String.format("Topic: %s, Type (TECHNICAL, HR, APTITUDE): %s", topic, type);
        
        String response = openAiClient.callOpenAi(systemPrompt, userPrompt);
        
        if (response != null) {
            try {
                if (response.startsWith("```json")) {
                    response = response.substring(7, response.length() - 3);
                } else if (response.startsWith("```")) {
                    response = response.substring(3, response.length() - 3);
                }
                return objectMapper.readValue(response, MockInterviewQuestionDTO.class);
            } catch (Exception e) {
                log.error("Failed to parse OpenAI response to MockInterviewQuestionDTO", e);
            }
        }
        
        // Fallback
        MockInterviewQuestionDTO fallback = new MockInterviewQuestionDTO();
        fallback.setQuestion("Can you explain the principles of Object-Oriented Programming?");
        fallback.setCategory(type != null ? type.toUpperCase() : "TECHNICAL");
        return fallback;
    }

    public MockInterviewFeedbackDTO evaluateAnswer(String question, String answer) {
        String systemPrompt = "You are an expert technical interviewer. Evaluate the student's answer to the interview question. Return ONLY a JSON object: { \"score\": double (0-100), \"feedback\": \"detailed paragraph\", \"improvementSuggestions\": [\"string\"] }";
        String userPrompt = String.format("Question: %s\nStudent Answer: %s", question, answer);
        
        String response = openAiClient.callOpenAi(systemPrompt, userPrompt);
        
        if (response != null) {
            try {
                if (response.startsWith("```json")) {
                    response = response.substring(7, response.length() - 3);
                } else if (response.startsWith("```")) {
                    response = response.substring(3, response.length() - 3);
                }
                return objectMapper.readValue(response, MockInterviewFeedbackDTO.class);
            } catch (Exception e) {
                log.error("Failed to parse OpenAI response to MockInterviewFeedbackDTO", e);
            }
        }
        
        // Fallback
        MockInterviewFeedbackDTO fallback = new MockInterviewFeedbackDTO();
        fallback.setScore(75.0);
        fallback.setFeedback("Good attempt. Your answer covers the basics but lacks depth and real-world examples.");
        fallback.setImprovementSuggestions(Arrays.asList("Provide a concrete example.", "Explain trade-offs."));
        return fallback;
    }
}
