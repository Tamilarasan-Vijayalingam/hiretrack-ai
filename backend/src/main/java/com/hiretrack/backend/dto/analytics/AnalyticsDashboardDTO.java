package com.hiretrack.backend.dto.analytics;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class AnalyticsDashboardDTO {
    private long totalStudents;
    private long eligibleStudents;
    private long placedStudents;
    private long unplacedStudents;
    private BigDecimal averagePackage;
    private BigDecimal highestPackage;
    private long activeDrives;
    private long upcomingDrives;
    private double placementPercentage;

    private List<Map<String, Object>> placementTrends; 
    private List<Map<String, Object>> departmentPlacements; 
    private List<Map<String, Object>> packageTrends; 
}
