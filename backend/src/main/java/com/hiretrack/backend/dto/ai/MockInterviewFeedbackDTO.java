package com.hiretrack.backend.dto.ai;

import lombok.Data;
import java.util.List;

@Data
public class MockInterviewFeedbackDTO {
    private double score;
    private String feedback;
    private List<String> improvementSuggestions;
}
