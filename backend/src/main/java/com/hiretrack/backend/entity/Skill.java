package com.hiretrack.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "skills")
public class Skill {
    @Id @GeneratedValue
    private UUID id;
    @Column(unique = true, nullable = false)
    private String name;
}
