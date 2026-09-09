package com.hiretrack.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "skills_gap_analysis")
public class SkillsGapAnalysis {
    @Id @GeneratedValue
    private UUID id;
    @ManyToOne @JoinColumn(name = "student_id")
    private Student student;
    @Column(name = "target_role")
    private String targetRole;
    @Column(name = "missing_skills")
    private String missingSkills;
    @Column(name = "recommended_courses")
    private String recommendedCourses;
    @Column(name = "learning_roadmap")
    private String learningRoadmap;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
