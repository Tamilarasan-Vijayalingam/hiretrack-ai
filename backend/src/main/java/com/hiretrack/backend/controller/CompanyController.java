package com.hiretrack.backend.controller;

import com.hiretrack.backend.dto.CompanyDTO;
import com.hiretrack.backend.dto.CompanySummaryDTO;
import com.hiretrack.backend.dto.DriveDTO;
import com.hiretrack.backend.dto.StudentEligibilityDetailDTO;
import com.hiretrack.backend.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping({"/api/companies", "/api/companys"})
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService service;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PLACEMENT_OFFICER', 'HOD', 'STUDENT')")
    public ResponseEntity<List<CompanyDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/summary")
    @PreAuthorize("hasAnyRole('ADMIN', 'PLACEMENT_OFFICER', 'HOD', 'STUDENT')")
    public ResponseEntity<CompanySummaryDTO> getSummary() {
        return ResponseEntity.ok(service.getSummary());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PLACEMENT_OFFICER', 'HOD', 'STUDENT')")
    public ResponseEntity<CompanyDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PLACEMENT_OFFICER')")
    public ResponseEntity<?> create(@Valid @RequestBody CompanyDTO dto) {
        try {
            CompanyDTO created = service.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PLACEMENT_OFFICER')")
    public ResponseEntity<?> update(@PathVariable UUID id, @Valid @RequestBody CompanyDTO dto) {
        try {
            CompanyDTO updated = service.update(id, dto);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        try {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/eligible-students")
    @PreAuthorize("hasAnyRole('ADMIN', 'PLACEMENT_OFFICER', 'HOD')")
    public ResponseEntity<List<StudentEligibilityDetailDTO>> getEligibleStudents(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getEligibleStudents(id));
    }

    @GetMapping("/{id}/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'PLACEMENT_OFFICER', 'HOD', 'STUDENT')")
    public ResponseEntity<CompanyDTO> getCompanyStatistics(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/{id}/drives")
    @PreAuthorize("hasAnyRole('ADMIN', 'PLACEMENT_OFFICER', 'HOD', 'STUDENT')")
    public ResponseEntity<List<DriveDTO>> getCompanyDrives(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getCompanyDrives(id));
    }
}
