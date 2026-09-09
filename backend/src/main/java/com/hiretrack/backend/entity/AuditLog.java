package com.hiretrack.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "audit_logs")
public class AuditLog {
    @Id @GeneratedValue
    private UUID id;
    @ManyToOne @JoinColumn(name = "user_id")
    private User user;
    @Column(nullable = false)
    private String action;
    @Column(nullable = false)
    private String entity;
    @Column(name = "entity_id")
    private UUID entityId;
    @CreationTimestamp
    private LocalDateTime timestamp;
}
