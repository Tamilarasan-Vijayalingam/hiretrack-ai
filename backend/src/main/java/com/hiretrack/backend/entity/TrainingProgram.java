package com.hiretrack.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "training_programs")
public class TrainingProgram {
    @Id @GeneratedValue
    private UUID id;
    @Column(nullable = false)
    private String title;
    private String description;
    @Column(name = "start_date")
    private java.time.LocalDate startDate;
    @Column(name = "end_date")
    private java.time.LocalDate endDate;
    @CreationTimestamp
    private LocalDateTime createdAt;
}
