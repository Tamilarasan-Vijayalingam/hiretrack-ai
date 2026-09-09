package com.hiretrack.backend.dto;

import lombok.Data;

@Data
public class CompanySummaryDTO {
    private long totalCompanies;
    private long activeHiringCompanies;
    private long totalOpenPositions;
    private String averagePackage;
}
