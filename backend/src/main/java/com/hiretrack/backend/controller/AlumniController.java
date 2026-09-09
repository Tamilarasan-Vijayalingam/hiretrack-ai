package com.hiretrack.backend.controller;

import com.hiretrack.backend.dto.AlumniDTO;
import com.hiretrack.backend.service.AlumniService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/alumnis")
@RequiredArgsConstructor
public class AlumniController {
    private final AlumniService service;

    @PostMapping
    public ResponseEntity<AlumniDTO> create(@RequestBody AlumniDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlumniDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<AlumniDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlumniDTO> update(@PathVariable UUID id, @RequestBody AlumniDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
