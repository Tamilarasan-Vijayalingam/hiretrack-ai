package com.hiretrack.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "alumni")
public class Alumni {
    @Id @GeneratedValue
    private UUID id;
    @OneToOne @JoinColumn(name = "student_id", unique = true)
    private Student student;
    @Column(name = "current_company")
    private String currentCompany;
    @Column(name = "\"current_role\"")
    private String currentRole;
    @Column(name = "package")
    private java.math.BigDecimal packageOffered;
    @CreationTimestamp
    private LocalDateTime createdAt;
}
