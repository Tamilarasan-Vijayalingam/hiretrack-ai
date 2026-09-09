package com.hiretrack.backend.controller;

import com.hiretrack.backend.dto.PlacementDTO;
import com.hiretrack.backend.service.PlacementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/placements")
@RequiredArgsConstructor
public class PlacementController {
    private final PlacementService service;

    @PostMapping
    public ResponseEntity<PlacementDTO> create(@RequestBody PlacementDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlacementDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<PlacementDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlacementDTO> update(@PathVariable UUID id, @RequestBody PlacementDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
