package com.hiretrack.backend.controller;

import com.hiretrack.backend.dto.AiPlacementReadinessDTO;
import com.hiretrack.backend.service.AiPlacementReadinessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/aiplacementreadinesss")
@RequiredArgsConstructor
public class AiPlacementReadinessController {
    private final AiPlacementReadinessService service;

    @PostMapping
    public ResponseEntity<AiPlacementReadinessDTO> create(@RequestBody AiPlacementReadinessDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AiPlacementReadinessDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<AiPlacementReadinessDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AiPlacementReadinessDTO> update(@PathVariable UUID id, @RequestBody AiPlacementReadinessDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
