package com.hiretrack.backend.service;

import com.hiretrack.backend.dto.analytics.AnalyticsDashboardDTO;
import com.hiretrack.backend.dto.analytics.TrainingAnalyticsDTO;
import com.hiretrack.backend.entity.*;
import com.hiretrack.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final StudentRepository studentRepository;
    private final PlacementRepository placementRepository;
    private final DriveRepository driveRepository;
    private final TrainingScoreRepository trainingScoreRepository;

    public AnalyticsDashboardDTO getDashboardStats(UUID departmentId) {
        AnalyticsDashboardDTO dto = new AnalyticsDashboardDTO();
        
        List<Student> allStudents = studentRepository.findAll();
        if (departmentId != null) {
            allStudents = allStudents.stream()
                .filter(s -> s.getDepartment() != null && s.getDepartment().getId().equals(departmentId))
                .toList();
        }
        
        List<Placement> allPlacements = placementRepository.findAll();
        if (departmentId != null) {
            Set<UUID> validStudentIds = allStudents.stream().map(Student::getId).collect(Collectors.toSet());
            allPlacements = allPlacements.stream()
                .filter(p -> p.getStudent() != null && validStudentIds.contains(p.getStudent().getId()))
                .toList();
        }

        long total = allStudents.size();
        long placed = allPlacements.size();
        long unplaced = total - placed;
        
        long eligible = allStudents.stream()
            .filter(s -> s.getCgpa() != null && s.getCgpa().doubleValue() >= 6.5 && (s.getBacklogs() == null || s.getBacklogs() <= 2))
            .count();

        BigDecimal highest = allPlacements.stream()
            .map(Placement::getPackageOffered)
            .filter(Objects::nonNull)
            .max(BigDecimal::compareTo)
            .orElse(BigDecimal.ZERO);

        BigDecimal average = BigDecimal.ZERO;
        if (placed > 0) {
            BigDecimal sum = allPlacements.stream()
                .map(Placement::getPackageOffered)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            average = sum.divide(BigDecimal.valueOf(placed), 2, RoundingMode.HALF_UP);
        }

        LocalDate today = LocalDate.now();
        List<Drive> allDrives = driveRepository.findAll();
        long activeDrives = allDrives.stream()
            .filter(d -> d.getRegistrationDeadline() != null && d.getRegistrationDeadline().isAfter(today))
            .count();
        long upcomingDrives = allDrives.stream()
            .filter(d -> d.getDriveDate() != null && d.getDriveDate().isAfter(today))
            .count();

        dto.setTotalStudents(total);
        dto.setEligibleStudents(eligible);
        dto.setPlacedStudents(placed);
        dto.setUnplacedStudents(unplaced);
        dto.setHighestPackage(highest);
        dto.setAveragePackage(average);
        dto.setActiveDrives(activeDrives);
        dto.setUpcomingDrives(upcomingDrives);
        dto.setPlacementPercentage(total > 0 ? ((double) placed / total) * 100 : 0);

        final List<Placement> finalPlacements = allPlacements;
        Map<String, Long> deptCounts = allStudents.stream()
            .filter(s -> s.getDepartment() != null && s.getDepartment().getName() != null)
            .filter(s -> finalPlacements.stream().anyMatch(p -> p.getStudent() != null && p.getStudent().getId().equals(s.getId())))
            .collect(Collectors.groupingBy(s -> s.getDepartment().getName(), Collectors.counting()));
            
        List<Map<String, Object>> deptTrends = new ArrayList<>();
        deptCounts.forEach((name, count) -> {
            Map<String, Object> map = new HashMap<>();
            map.put("name", name);
            map.put("value", count);
            deptTrends.add(map);
        });
        dto.setDepartmentPlacements(deptTrends);

        List<Map<String, Object>> placementsTrends = new ArrayList<>();
        Map<java.time.Month, Long> placementsByMonth = allPlacements.stream()
            .filter(p -> p.getPlacementDate() != null)
            .collect(Collectors.groupingBy(p -> p.getPlacementDate().getMonth(), Collectors.counting()));
        
        java.time.Month currentMonth = LocalDate.now().getMonth();
        for (int i = 5; i >= 0; i--) {
            java.time.Month m = currentMonth.minus(i);
            Map<String, Object> map = new HashMap<>();
            map.put("name", m.name().substring(0, 3));
            map.put("placements", placementsByMonth.getOrDefault(m, 0L));
            placementsTrends.add(map);
        }
        dto.setPlacementTrends(placementsTrends);

        return dto;
    }

    public TrainingAnalyticsDTO getTrainingAnalytics() {
        List<TrainingScore> scores = trainingScoreRepository.findAll();
        TrainingAnalyticsDTO dto = new TrainingAnalyticsDTO();
        dto.setProgramName("Pre-Placement Training 2024");
        
        if (scores.isEmpty()) {
            dto.setBeforeAptitude(0.0);
            dto.setAfterAptitude(0.0);
            dto.setBeforeCoding(0.0);
            dto.setAfterCoding(0.0);
            dto.setBeforeInterview(0.0);
            dto.setAfterInterview(0.0);
        } else {
            double bApt = scores.stream().filter(s -> s.getBeforeAptitude() != null).mapToDouble(TrainingScore::getBeforeAptitude).average().orElse(0);
            double aApt = scores.stream().filter(s -> s.getAfterAptitude() != null).mapToDouble(TrainingScore::getAfterAptitude).average().orElse(0);
            double bCod = scores.stream().filter(s -> s.getBeforeCoding() != null).mapToDouble(TrainingScore::getBeforeCoding).average().orElse(0);
            double aCod = scores.stream().filter(s -> s.getAfterCoding() != null).mapToDouble(TrainingScore::getAfterCoding).average().orElse(0);
            double bInt = scores.stream().filter(s -> s.getBeforeInterview() != null).mapToDouble(TrainingScore::getBeforeInterview).average().orElse(0);
            double aInt = scores.stream().filter(s -> s.getAfterInterview() != null).mapToDouble(TrainingScore::getAfterInterview).average().orElse(0);
            
            dto.setBeforeAptitude(bApt);
            dto.setAfterAptitude(aApt);
            dto.setBeforeCoding(bCod);
            dto.setAfterCoding(aCod);
            dto.setBeforeInterview(bInt);
            dto.setAfterInterview(aInt);
        }
        
        dto.setAptitudeImprovementPercentage(calculateImprovement(dto.getBeforeAptitude(), dto.getAfterAptitude()));
        dto.setCodingImprovementPercentage(calculateImprovement(dto.getBeforeCoding(), dto.getAfterCoding()));
        dto.setInterviewImprovementPercentage(calculateImprovement(dto.getBeforeInterview(), dto.getAfterInterview()));
        
        return dto;
    }
    
    private double calculateImprovement(double before, double after) {
        if (before == 0) return 100.0;
        return ((after - before) / before) * 100.0;
    }
}
