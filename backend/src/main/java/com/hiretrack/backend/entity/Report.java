package com.hiretrack.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "reports")
public class Report {
    @Id @GeneratedValue
    private UUID id;
    @ManyToOne @JoinColumn(name = "generated_by")
    private User generatedBy;
    @Column(name = "report_type", nullable = false)
    private String reportType;
    @Column(name = "data_url")
    private String dataUrl;
    @CreationTimestamp
    private LocalDateTime createdAt;
}
