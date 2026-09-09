package com.hiretrack.backend.controller;

import com.hiretrack.backend.dto.ai.*;
import com.hiretrack.backend.entity.Drive;
import com.hiretrack.backend.entity.Student;
import com.hiretrack.backend.repository.DriveRepository;
import com.hiretrack.backend.repository.StudentRepository;
import com.hiretrack.backend.service.ai.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final EligibilityEngineService eligibilityEngineService;
    private final ReadinessService readinessService;
    private final PredictionService predictionService;
    private final SkillsGapService skillsGapService;
    private final AtsAnalyzerService atsAnalyzerService;
    private final RiskDetectionService riskDetectionService;

    private final StudentRepository studentRepository;
    private final DriveRepository driveRepository;

    @GetMapping("/eligibility/{studentId}/{driveId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PLACEMENT_OFFICER') or @securityService.canAccessStudentData(authentication, #studentId)")
    public ResponseEntity<EligibilityResultDTO> checkEligibility(@PathVariable UUID studentId, @PathVariable UUID driveId) {
        Student student = studentRepository.findById(studentId).orElseThrow();
        Drive drive = driveRepository.findById(driveId).orElseThrow();
        return ResponseEntity.ok(eligibilityEngineService.checkEligibility(student, drive));
    }

    @GetMapping("/readiness/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PLACEMENT_OFFICER', 'HOD') or @securityService.canAccessStudentData(authentication, #studentId)")
    public ResponseEntity<ReadinessScoreDTO> getReadinessScore(@PathVariable UUID studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow();
        return ResponseEntity.ok(readinessService.calculateReadiness(student));
    }

    @GetMapping("/prediction/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PLACEMENT_OFFICER') or @securityService.canAccessStudentData(authentication, #studentId)")
    public ResponseEntity<PredictionDTO> getPrediction(@PathVariable UUID studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow();
        ReadinessScoreDTO readiness = readinessService.calculateReadiness(student);
        return ResponseEntity.ok(predictionService.predictPlacement(student, readiness));
    }

    @GetMapping("/skills-gap/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PLACEMENT_OFFICER') or @securityService.canAccessStudentData(authentication, #studentId)")
    public ResponseEntity<SkillsGapDTO> analyzeSkillsGap(@PathVariable UUID studentId, @RequestParam String targetRole) {
        return ResponseEntity.ok(skillsGapService.analyzeGap(targetRole));
    }

    @PostMapping("/resume/analyze")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    public ResponseEntity<?> analyzeResume(@RequestBody String resumeText) {
        if (resumeText == null || resumeText.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Resume text cannot be empty");
        }
        if (resumeText.length() > 10000) {
            return ResponseEntity.badRequest().body("Resume text exceeds maximum length of 10,000 characters");
        }
        return ResponseEntity.ok(atsAnalyzerService.analyzeResume(resumeText));
    }

    @GetMapping("/risk/department/{departmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOD')")
    public ResponseEntity<List<RiskAssessmentDTO>> detectRisks(@PathVariable UUID departmentId) {
        List<Student> students = studentRepository.findAll().stream()
            .filter(s -> s.getDepartment() != null && s.getDepartment().getId().equals(departmentId))
            .collect(Collectors.toList());
            
        List<RiskAssessmentDTO> risks = students.stream()
            .map(riskDetectionService::assessRisk)
            .filter(dto -> !dto.getRiskLevel().equals("LOW"))
            .collect(Collectors.toList());
            
        return ResponseEntity.ok(risks);
    }
}
