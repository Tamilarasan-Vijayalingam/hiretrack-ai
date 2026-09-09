package com.hiretrack.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "ai_placement_predictions")
public class AiPlacementPrediction {
    @Id @GeneratedValue
    private UUID id;
    @OneToOne @JoinColumn(name = "student_id", unique = true)
    private Student student;
    @Column(name = "success_probability")
    private java.math.BigDecimal successProbability;
    private java.math.BigDecimal confidence;
    private String strengths;
    private String weaknesses;
    @Column(name = "improvement_suggestions")
    private String improvementSuggestions;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
