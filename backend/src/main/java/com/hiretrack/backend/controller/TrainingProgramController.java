package com.hiretrack.backend.controller;

import com.hiretrack.backend.dto.TrainingProgramDTO;
import com.hiretrack.backend.service.TrainingProgramService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/trainingprograms")
@RequiredArgsConstructor
public class TrainingProgramController {
    private final TrainingProgramService service;

    @PostMapping
    public ResponseEntity<TrainingProgramDTO> create(@RequestBody TrainingProgramDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrainingProgramDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<TrainingProgramDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrainingProgramDTO> update(@PathVariable UUID id, @RequestBody TrainingProgramDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
