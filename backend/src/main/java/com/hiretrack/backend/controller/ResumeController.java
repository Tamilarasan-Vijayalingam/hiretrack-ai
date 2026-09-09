package com.hiretrack.backend.controller;

import com.hiretrack.backend.dto.ResumeDTO;
import com.hiretrack.backend.service.ResumeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/resumes")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'PLACEMENT_OFFICER', 'HOD', 'STUDENT')")
public class ResumeController {
    private final ResumeService service;

    @PostMapping
    public ResponseEntity<ResumeDTO> create(@RequestBody ResumeDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResumeDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<ResumeDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResumeDTO> update(@PathVariable UUID id, @RequestBody ResumeDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
