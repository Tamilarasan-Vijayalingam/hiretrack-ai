package com.hiretrack.backend.controller;

import com.hiretrack.backend.dto.analytics.AnalyticsDashboardDTO;
import com.hiretrack.backend.dto.analytics.TrainingAnalyticsDTO;
import com.hiretrack.backend.entity.User;
import com.hiretrack.backend.repository.UserRepository;
import com.hiretrack.backend.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;
    private final UserRepository userRepository;

    @GetMapping("/admin")
    @PreAuthorize("hasAnyRole('ADMIN', 'PLACEMENT_OFFICER')")
    public ResponseEntity<AnalyticsDashboardDTO> getAdminAnalytics() {
        return ResponseEntity.ok(analyticsService.getDashboardStats(null));
    }

    @GetMapping("/hod")
    @PreAuthorize("hasRole('HOD')")
    public ResponseEntity<AnalyticsDashboardDTO> getHodAnalytics(Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow();
        UUID departmentId = user.getDepartment() != null ? user.getDepartment().getId() : null;
        if (departmentId == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(analyticsService.getDashboardStats(departmentId));
    }

    @GetMapping("/training")
    @PreAuthorize("hasAnyRole('ADMIN', 'PLACEMENT_OFFICER', 'HOD')")
    public ResponseEntity<TrainingAnalyticsDTO> getTrainingAnalytics() {
        return ResponseEntity.ok(analyticsService.getTrainingAnalytics());
    }
}
