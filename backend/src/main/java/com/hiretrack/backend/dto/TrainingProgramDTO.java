package com.hiretrack.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TrainingProgramDTO {
    private UUID id;
    private String title;
    private String description;
    private java.time.LocalDate startDate;
    private java.time.LocalDate endDate;
    private LocalDateTime createdAt;
}
