package com.hiretrack.backend.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class StudentEligibilityDetailDTO {
    private UUID studentId;
    private String name;
    private String email;
    private String registerNumber;
    private String department;
    private BigDecimal cgpa;
    private Integer backlogs;
    private List<String> skills = new ArrayList<>();
    private String certifications;
    private boolean eligible;
    private List<String> reasons = new ArrayList<>();
    private Double matchPercentage = 0.0;
}
