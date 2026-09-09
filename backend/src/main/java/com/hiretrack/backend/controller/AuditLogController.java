package com.hiretrack.backend.controller;

import com.hiretrack.backend.dto.AuditLogDTO;
import com.hiretrack.backend.service.AuditLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/auditlogs")
@RequiredArgsConstructor
public class AuditLogController {
    private final AuditLogService service;

    @PostMapping
    public ResponseEntity<AuditLogDTO> create(@RequestBody AuditLogDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuditLogDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<AuditLogDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuditLogDTO> update(@PathVariable UUID id, @RequestBody AuditLogDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
