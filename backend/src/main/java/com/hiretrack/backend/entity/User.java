package com.hiretrack.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id @GeneratedValue
    private UUID id;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "password_hash", nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    @org.hibernate.annotations.JdbcType(org.hibernate.dialect.PostgreSQLEnumJdbcType.class)
    @Column(nullable = false)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(name = "is_active")
    private Boolean isActive;
    @CreationTimestamp @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
