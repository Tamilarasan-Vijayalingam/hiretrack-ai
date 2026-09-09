package com.hiretrack.backend.service.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class OpenAiClient {

    @Value("${openai.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String callOpenAi(String systemPrompt, String userPrompt) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            log.warn("OPENAI_API_KEY is not configured. Returning fallback response.");
            return null;
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            Map<String, Object> requestBody = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(
                    Map.of("role", "system", "content", systemPrompt),
                    Map.of("role", "user", "content", userPrompt)
                ),
                "temperature", 0.7
            );

            HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);

            ResponseEntity<String> response = restTemplate.postForEntity(
                "https://api.openai.com/v1/chat/completions",
                entity,
                String.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                JsonNode root = objectMapper.readTree(response.getBody());
                return root.path("choices").get(0).path("message").path("content").asText();
            } else {
                log.error("OpenAI API call failed: {}", response.getStatusCode());
                return null;
            }
        } catch (Exception e) {
            log.error("Exception during OpenAI API call", e);
            return null;
        }
    }
}
