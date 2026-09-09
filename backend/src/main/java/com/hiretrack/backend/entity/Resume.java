package com.hiretrack.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "resumes")
public class Resume {
    @Id @GeneratedValue
    private UUID id;
    @ManyToOne @JoinColumn(name = "student_id")
    private Student student;
    @Column(name = "ats_score")
    private Integer atsScore;
    @Column(name = "missing_keywords")
    private String missingKeywords;
    @Column(name = "missing_skills")
    private String missingSkills;
    private String feedback;
    @CreationTimestamp
    private LocalDateTime uploadedAt;
}
