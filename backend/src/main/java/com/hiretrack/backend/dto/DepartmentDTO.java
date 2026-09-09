package com.hiretrack.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class DepartmentDTO {
    private UUID id;
    private String name;
    private String code;
}
