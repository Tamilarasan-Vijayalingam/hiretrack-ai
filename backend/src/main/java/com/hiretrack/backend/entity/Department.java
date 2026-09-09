package com.hiretrack.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "departments")
public class Department {
    @Id @GeneratedValue
    private UUID id;
    @Column(unique = true, nullable = false)
    private String name;
    @Column(unique = true, nullable = false)
    private String code;
}
