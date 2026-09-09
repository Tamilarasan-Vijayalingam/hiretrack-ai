package com.hiretrack.backend.controller;

import com.hiretrack.backend.dto.DriveDTO;
import com.hiretrack.backend.service.DriveService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/drives")
@RequiredArgsConstructor
public class DriveController {
    private final DriveService service;

    @PostMapping
    public ResponseEntity<DriveDTO> create(@RequestBody DriveDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DriveDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<DriveDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DriveDTO> update(@PathVariable UUID id, @RequestBody DriveDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
