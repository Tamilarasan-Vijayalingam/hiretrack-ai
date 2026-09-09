package com.hiretrack.backend.controller;

import com.hiretrack.backend.dto.TrainingScoreDTO;
import com.hiretrack.backend.service.TrainingScoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/trainingscores")
@RequiredArgsConstructor
public class TrainingScoreController {
    private final TrainingScoreService service;

    @PostMapping
    public ResponseEntity<TrainingScoreDTO> create(@RequestBody TrainingScoreDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrainingScoreDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<TrainingScoreDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrainingScoreDTO> update(@PathVariable UUID id, @RequestBody TrainingScoreDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
