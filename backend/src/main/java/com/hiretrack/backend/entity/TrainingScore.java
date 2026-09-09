package com.hiretrack.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "training_scores")
public class TrainingScore {
    @Id @GeneratedValue
    private UUID id;
    @ManyToOne @JoinColumn(name = "student_id")
    private Student student;
    @ManyToOne @JoinColumn(name = "training_program_id")
    private TrainingProgram trainingProgram;
    @Column(name = "before_aptitude")
    private Integer beforeAptitude;
    @Column(name = "after_aptitude")
    private Integer afterAptitude;
    @Column(name = "before_coding")
    private Integer beforeCoding;
    @Column(name = "after_coding")
    private Integer afterCoding;
    @Column(name = "before_interview")
    private Integer beforeInterview;
    @Column(name = "after_interview")
    private Integer afterInterview;
    @Column(name = "improvement_percentage")
    private java.math.BigDecimal improvementPercentage;
}
