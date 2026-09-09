package com.hiretrack.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "students")
public class Student {
    @Id @GeneratedValue
    private UUID id;
    @OneToOne @JoinColumn(name = "user_id", unique = true)
    private User user;
    @Column(nullable = false)
    private String name;
    @Column(name = "register_number", unique = true, nullable = false)
    private String registerNumber;
    @Column(unique = true, nullable = false)
    private String email;
    private String phone;
    @ManyToOne @JoinColumn(name = "department_id")
    private Department department;
    private java.math.BigDecimal cgpa;
    private Integer backlogs;
    private String certifications;
    private String projects;
    @Column(name = "profile_completion")
    private Integer profileCompletion;
    @Column(name = "batch_year")
    private Integer batchYear;
    @Column(name = "graduation_year")
    private Integer graduationYear;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
