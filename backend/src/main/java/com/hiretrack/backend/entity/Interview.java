package com.hiretrack.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "interviews")
public class Interview {
    @Id @GeneratedValue
    private UUID id;
    @ManyToOne @JoinColumn(name = "student_id")
    private Student student;
    @ManyToOne @JoinColumn(name = "drive_id")
    private Drive drive;
    @Column(name = "interview_type", nullable = false)
    private String interviewType;
    @Column(nullable = false)
    private LocalDateTime date;
    private String questions;
    private String answers;
    @Column(name = "technical_score")
    private Integer technicalScore;
    @Column(name = "hr_score")
    private Integer hrScore;
    @Column(name = "aptitude_score")
    private Integer aptitudeScore;
    private String feedback;
    @CreationTimestamp
    private LocalDateTime createdAt;
}
