package com.hiretrack.backend.service.ai;

import com.hiretrack.backend.dto.ai.RiskAssessmentDTO;
import com.hiretrack.backend.entity.Student;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RiskDetectionService {

    @Value("${risk.threshold.cgpa:6.5}")
    private double cgpaThreshold;

    @Value("${risk.threshold.backlogs:2}")
    private int backlogThreshold;

    public RiskAssessmentDTO assessRisk(Student student) {
        RiskAssessmentDTO dto = new RiskAssessmentDTO();
        dto.setStudentId(student.getId());
        dto.setStudentName(student.getName());
        
        List<String> riskFactors = new ArrayList<>();
        int riskScore = 0;

        if (student.getCgpa() != null && student.getCgpa().doubleValue() < cgpaThreshold) {
            riskFactors.add("CGPA below threshold (" + student.getCgpa() + " < " + cgpaThreshold + ")");
            riskScore += 2;
        }

        if (student.getBacklogs() != null && student.getBacklogs() > backlogThreshold) {
            riskFactors.add("Excessive backlogs (" + student.getBacklogs() + ")");
            riskScore += 3;
        }

        if (riskScore >= 4) {
            dto.setRiskLevel("CRITICAL");
            dto.setInterventionRecommendation("Immediate HOD counseling required. Enforce mandatory remedial classes.");
        } else if (riskScore > 0) {
            dto.setRiskLevel("MEDIUM");
            dto.setInterventionRecommendation("Assign to peer-mentoring program.");
        } else {
            dto.setRiskLevel("LOW");
            dto.setInterventionRecommendation("No intervention needed.");
        }

        dto.setRiskFactors(riskFactors);
        return dto;
    }
}
