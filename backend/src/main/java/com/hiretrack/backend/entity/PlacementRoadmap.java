package com.hiretrack.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "placement_roadmaps")
public class PlacementRoadmap {
    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne
    @JoinColumn(name = "student_id", unique = true, nullable = false)
    private Student student;

    @Column(name = "target_role")
    private String targetRole;

    @Column(name = "roadmap_data", columnDefinition = "TEXT")
    private String roadmapData;

    @Column(name = "current_week")
    private Integer currentWeek = 1;

    @Column(name = "status")
    private String status = "IN_PROGRESS";

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
