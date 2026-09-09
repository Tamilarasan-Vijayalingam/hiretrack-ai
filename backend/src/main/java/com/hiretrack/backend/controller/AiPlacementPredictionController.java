package com.hiretrack.backend.controller;

import com.hiretrack.backend.dto.AiPlacementPredictionDTO;
import com.hiretrack.backend.service.AiPlacementPredictionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/aiplacementpredictions")
@RequiredArgsConstructor
public class AiPlacementPredictionController {
    private final AiPlacementPredictionService service;

    @PostMapping
    public ResponseEntity<AiPlacementPredictionDTO> create(@RequestBody AiPlacementPredictionDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AiPlacementPredictionDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<AiPlacementPredictionDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AiPlacementPredictionDTO> update(@PathVariable UUID id, @RequestBody AiPlacementPredictionDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
