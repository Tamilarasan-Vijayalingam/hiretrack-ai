package com.hiretrack.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "notifications")
public class Notification {
    @Id @GeneratedValue
    private UUID id;
    @ManyToOne @JoinColumn(name = "user_id")
    private User user;
    private String type;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String message;
    @Column(name = "is_read")
    private Boolean isRead;
    @Column(name = "scheduled_time")
    private LocalDateTime scheduledTime;
    @CreationTimestamp
    private LocalDateTime createdAt;
}
