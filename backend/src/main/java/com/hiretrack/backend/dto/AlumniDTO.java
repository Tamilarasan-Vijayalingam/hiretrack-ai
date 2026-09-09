package com.hiretrack.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AlumniDTO {
    private UUID id;
    private UUID studentId;
    private String currentCompany;
    private String currentRole;
    private java.math.BigDecimal packageOffered;
    private LocalDateTime createdAt;
}
