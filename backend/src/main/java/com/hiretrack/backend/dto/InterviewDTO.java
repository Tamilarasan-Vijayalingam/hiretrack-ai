package com.hiretrack.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class InterviewDTO {
    private UUID id;
    private UUID studentId;
    private UUID driveId;
    private String interviewType;
    private LocalDateTime date;
    private String questions;
    private String answers;
    private Integer technicalScore;
    private Integer hrScore;
    private Integer aptitudeScore;
    private String feedback;
    private LocalDateTime createdAt;
}
