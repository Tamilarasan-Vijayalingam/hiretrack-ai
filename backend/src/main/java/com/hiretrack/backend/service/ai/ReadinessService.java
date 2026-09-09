package com.hiretrack.backend.service.ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hiretrack.backend.dto.ai.ReadinessScoreDTO;
import com.hiretrack.backend.entity.Student;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReadinessService {

    private final OpenAiClient openAiClient;

    public ReadinessScoreDTO calculateReadiness(Student student) {
        ReadinessScoreDTO dto = new ReadinessScoreDTO();
        
        double cgpa = student.getCgpa() != null ? student.getCgpa().doubleValue() : 0.0;
        double academicScore = (cgpa / 10.0) * 30.0;
        dto.setAcademicScore(academicScore);

        // Simulation values that would eventually be derived from DB counts
        double skillsScore = 20.0; // out of 25
        double projectsScore = 12.0; // out of 15
        double certificationsScore = 8.0; // out of 10
        double resumeScore = 8.5; // out of 10
        double interviewScore = 7.0; // out of 10

        dto.setSkillsScore(skillsScore);
        dto.setProjectsScore(projectsScore);
        dto.setCertificationsScore(certificationsScore);
        dto.setResumeScore(resumeScore);
        dto.setInterviewScore(interviewScore);

        double total = academicScore + skillsScore + projectsScore + certificationsScore + resumeScore + interviewScore;
        dto.setOverallScore(Math.min(100.0, total));

        // Use AI to generate qualitative feedback based on the scores
        String systemPrompt = "You are a career counselor. Based on the provided readiness sub-scores (0-100 total), write a short, encouraging 2-sentence feedback paragraph for the student.";
        String userPrompt = String.format("Overall: %.1f, Academic: %.1f/30, Skills: %.1f/25, Projects: %.1f/15, Certifications: %.1f/10, Resume: %.1f/10, Interview: %.1f/10", 
            dto.getOverallScore(), academicScore, skillsScore, projectsScore, certificationsScore, resumeScore, interviewScore);
            
        String feedback = openAiClient.callOpenAi(systemPrompt, userPrompt);
        
        if (feedback != null) {
            dto.setFeedback(feedback);
        } else {
            dto.setFeedback("Your overall readiness score is good. Focus on improving your lowest sub-scores to maximize your placement chances.");
        }

        return dto;
    }
}
