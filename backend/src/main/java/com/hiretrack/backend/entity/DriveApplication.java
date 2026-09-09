package com.hiretrack.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "drive_applications")
public class DriveApplication {
    @Id @GeneratedValue
    private UUID id;
    @ManyToOne @JoinColumn(name = "student_id")
    private Student student;
    @ManyToOne @JoinColumn(name = "drive_id")
    private Drive drive;
    private String status;
    @CreationTimestamp
    private LocalDateTime appliedAt;
}
