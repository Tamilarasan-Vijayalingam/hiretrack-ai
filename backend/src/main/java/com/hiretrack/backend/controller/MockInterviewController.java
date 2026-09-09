package com.hiretrack.backend.controller;

import com.hiretrack.backend.dto.ai.MockInterviewFeedbackDTO;
import com.hiretrack.backend.dto.ai.MockInterviewQuestionDTO;
import com.hiretrack.backend.service.ai.MockInterviewAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ai/mock-interview")
@RequiredArgsConstructor
public class MockInterviewController {

    private final MockInterviewAiService mockInterviewAiService;

    @PostMapping("/generate")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    public ResponseEntity<MockInterviewQuestionDTO> generateQuestion(@RequestBody Map<String, String> request) {
        String topic = request.getOrDefault("topic", "Software Engineering");
        String type = request.getOrDefault("type", "TECHNICAL");
        return ResponseEntity.ok(mockInterviewAiService.generateQuestion(topic, type));
    }

    @PostMapping("/evaluate")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    public ResponseEntity<MockInterviewFeedbackDTO> evaluateAnswer(@RequestBody Map<String, String> request) {
        String question = request.get("question");
        String answer = request.get("answer");
        
        if (question == null || answer == null) {
            return ResponseEntity.badRequest().build();
        }
        
        return ResponseEntity.ok(mockInterviewAiService.evaluateAnswer(question, answer));
    }
}
