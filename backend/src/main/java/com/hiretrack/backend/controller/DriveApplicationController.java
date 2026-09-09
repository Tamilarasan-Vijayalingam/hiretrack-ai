package com.hiretrack.backend.controller;

import com.hiretrack.backend.dto.DriveApplicationDTO;
import com.hiretrack.backend.service.DriveApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/driveapplications")
@RequiredArgsConstructor
public class DriveApplicationController {
    private final DriveApplicationService service;

    @PostMapping
    public ResponseEntity<DriveApplicationDTO> create(@RequestBody DriveApplicationDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DriveApplicationDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<DriveApplicationDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DriveApplicationDTO> update(@PathVariable UUID id, @RequestBody DriveApplicationDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
