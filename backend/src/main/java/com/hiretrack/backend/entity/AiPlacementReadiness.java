package com.hiretrack.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "ai_placement_readiness")
public class AiPlacementReadiness {
    @Id @GeneratedValue
    private UUID id;
    @OneToOne @JoinColumn(name = "student_id", unique = true)
    private Student student;
    private Integer score;
    @Column(name = "readiness_breakdown")
    private String readinessBreakdown;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
