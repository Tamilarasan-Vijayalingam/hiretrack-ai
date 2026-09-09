package com.hiretrack.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CourseDTO {
    private UUID id;
    private String name;
    private String platform;
    private String url;
}
