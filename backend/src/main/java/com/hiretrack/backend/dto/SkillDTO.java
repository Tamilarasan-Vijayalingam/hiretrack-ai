package com.hiretrack.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class SkillDTO {
    private UUID id;
    private String name;
}
